package com.zachary_moore.annoying_spam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int mNumberOfMessages;
    private String mNumberToSendTo;
    private String mMessageToSend;
    private SmsManager mSmsMan = SmsManager.getDefault();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button annoy = (Button) findViewById(R.id.sendButton);
        mContext = this;

        //Ask for permissions
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        //ActionListener for button
        annoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Info
                mNumberToSendTo = ((EditText) findViewById(R.id.numberToSend)).getText().toString();
                mMessageToSend = ((EditText) findViewById(R.id.messageToSend)).getText().toString();
                EditText editText = (EditText) findViewById(R.id.numberOfMessages);
                String numberOfMessagesStr = editText.getText().toString();
                if (numberOfMessagesStr.equals("")) {
                    numberOfMessagesStr = "0";
                }
                int mNumberOfMessages = Integer.parseInt(numberOfMessagesStr);

                //Handle Incorrect Params
                if (mNumberToSendTo.equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Incorrect Params")
                            .setMessage("You do not have a phone number entered.")
                            .setNeutralButton("OK", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    //Confirmation for sending
                    AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Send Messages?")
                            .setMessage(String.format("Are you sure you want to send:\n\"%s\"\n\n%d times\n\nTo the number:\n%s", mMessageToSend, mNumberOfMessages, mNumberToSendTo))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendMessages();

                                    //Display Messages Sent
                                    Toast.makeText(MainActivity.this, "Messages Sent", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", null);

                    //Used to center message text
                    AlertDialog confirmDialog = confirmDialogBuilder.show();
                    TextView confirmMessage = (TextView)confirmDialog.findViewById(android.R.id.message);
                    confirmMessage.setGravity(Gravity.CENTER);

                }
            }
        });

    }

    //Send data in edit text fields for speified number of messages
    public void sendMessages() {
        int count = mNumberOfMessages;
        while (count > 0) {
            mSmsMan.sendTextMessage(mNumberToSendTo, null, mMessageToSend, null, null);
            count--;
        }
    }
}
