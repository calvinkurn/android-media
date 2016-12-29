package com.tokopedia.core.msisdn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nisie on 7/14/16.
 */
public class IncomingSms extends BroadcastReceiver {

    public interface ReceiveSMSListener {
        void onReceiveOTP(String otpCode);
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
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if (senderNum.equals("Tokopedia") || message.startsWith("Tokopedia")) {

                            CommonUtils.dumper("NISNISSMS " + message);
                            String regexString = Pattern.quote("Tokopedia - ") + "(.*?)" + Pattern.quote("adalah");
                            Pattern pattern = Pattern.compile(regexString);
                            Matcher matcher = pattern.matcher(message);

                            while (matcher.find()) {
                                String otpCode = matcher.group(1).trim();
                                if (listener != null)
                                    listener.onReceiveOTP(otpCode);
                            }


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

    public void registerSMSReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        context.registerReceiver(this, filter);
    }

    public void setListener(ReceiveSMSListener listener) {
        this.listener = listener;
    }
}