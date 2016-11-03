package com.tokopedia.tkpd.msisdn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Nisie on 7/14/16.
 */
public class IncomingSms extends BroadcastReceiver {

    public interface ReceiveSMSListener {
        void onReceiveSMS(String message);
    }

    ReceiveSMSListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage;
                    if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        currentMessage = msgs[0];
                    } else {
                        Object pdus[] = (Object[]) bundle.get("pdus");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    }
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if (senderNum.equals("Tokopedia") || message.startsWith("[Tokopedia]")) {
                            if (listener != null)
                                listener.onReceiveSMS(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(IncomingSms.class.getSimpleName(), e.toString());
                    }
                }
            }

        } catch (Exception e) {
            Log.e(IncomingSms.class.getSimpleName(), e.toString());
        }
    }

    public void setListener(ReceiveSMSListener listener) {
        this.listener = listener;
    }
}