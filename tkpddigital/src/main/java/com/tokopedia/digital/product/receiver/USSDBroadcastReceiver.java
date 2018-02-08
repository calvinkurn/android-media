package com.tokopedia.digital.product.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.digital.product.listener.IUssdUpdateListener;


/**
 * Created by ashwanityagi on 28/06/17.
 */

public class USSDBroadcastReceiver extends BroadcastReceiver {
    public static final String EXTRA_RESULT_USSD_DATA = "EXTRA_RESULT_USSD_DATA";
    public static final String EXTRA_USSD_MESSAGE_FAILED = "EXTRA_USSD_MESSAGE_FAILED";
    public static final String ACTION_GET_BALANCE_FROM_USSD = USSDBroadcastReceiver
            .class.getCanonicalName() + ".ACTION_GET_BALANCE_FROM_USSD";
    private IUssdUpdateListener listener;

    public USSDBroadcastReceiver(IUssdUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras().containsKey(EXTRA_RESULT_USSD_DATA)) {
            listener.onReceivedUssdData(intent
                    .getStringExtra(EXTRA_RESULT_USSD_DATA));
        } else {
            listener.onUssdDataError(intent.getExtras()
                    .getString(EXTRA_USSD_MESSAGE_FAILED));
        }
    }
}