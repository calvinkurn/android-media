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
import com.tokopedia.core.util.MethodChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nisie on 7/14/16.
 */
public class IncomingSms extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    public interface ReceiveSMSListener {
        void onReceiveOTP(String otpCode);
    }

    ReceiveSMSListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj != null) {
                SmsMessage currentMessage;
                Object pdus[] = (Object[]) bundle.get("pdus");
                currentMessage = MethodChecker.createSmsFromPdu(intent, (byte[]) (pdus != null ? pdus[0] : ""));

                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                if (senderNum.equals("Tokopedia") || message.startsWith("Tokopedia")) {

                    String regexString = Pattern.quote("Tokopedia - ") + "(.*?)" + Pattern.quote("adalah");
                    Pattern pattern = Pattern.compile(regexString);
                    Matcher matcher = pattern.matcher(message);

                    while (matcher.find()) {
                        String otpCode = matcher.group(1).trim();
                        if (listener != null)
                            listener.onReceiveOTP(otpCode);
                    }


                }
            }
        }
    }

    public void registerSMSReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SMS_RECEIVED);
        context.registerReceiver(this, filter);
    }

    public void setListener(ReceiveSMSListener listener) {
        this.listener = listener;
    }
}