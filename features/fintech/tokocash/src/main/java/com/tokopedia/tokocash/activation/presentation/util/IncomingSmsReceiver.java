package com.tokopedia.tokocash.activation.presentation.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * @author by Nisie on 7/14/16.
 */
public class IncomingSmsReceiver extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TOKOPEDIA_PREFIX_SMS = "Tokopedia";
    private static final String ADALAH_SMS = "adalah";

    public interface ReceiveSMSListener {
        void onReceiveOTP(String otpCode);
    }

    ReceiveSMSListener listener;

    @Inject
    public IncomingSmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            final Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SmsMessage currentMessage;
                if (MethodChecker.createSmsFromPdu(intent) != null) {
                    currentMessage = MethodChecker.createSmsFromPdu(intent);

                    if (isTokopediaOtpSms(currentMessage)) {
                        String regexString = Pattern.quote(TOKOPEDIA_PREFIX_SMS + " - ") + "(.*?)" + Pattern.quote(ADALAH_SMS);
                        Pattern pattern = Pattern.compile(regexString);
                        Matcher matcher = pattern.matcher(currentMessage.getDisplayMessageBody());

                        while (matcher.find()) {
                            String otpCode = matcher.group(1).trim();
                            if (listener != null) {
                                listener.onReceiveOTP(otpCode);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isTokopediaOtpSms(SmsMessage currentMessage) {
        String senderNum = currentMessage.getDisplayOriginatingAddress() != null ?
                currentMessage.getDisplayOriginatingAddress() : "";
        String message = currentMessage.getDisplayMessageBody() != null ?
                currentMessage.getDisplayMessageBody() : "";

        return (senderNum != null && senderNum.equals(TOKOPEDIA_PREFIX_SMS)) ||
                (message != null && message.startsWith(TOKOPEDIA_PREFIX_SMS));
    }

    public void registerSmsReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SMS_RECEIVED);
        context.registerReceiver(this, filter);
    }

    public void setListener(ReceiveSMSListener listener) {
        this.listener = listener;
    }
}