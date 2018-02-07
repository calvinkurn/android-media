package com.tokopedia.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ricoharisin on 7/26/16.
 */
public class ErrorNetworkReceiver extends BroadcastReceiver {

    private ReceiveListener mReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null) {
            if (intent.getAction() == null) {
                Log.e(this.getClass().getSimpleName(), "Intent Action NULL");
                return;
            }
            if (intent.getAction().equals("com.tokopedia.tkpd.FORCE_LOGOUT")) {
                mReceiver.onForceLogout();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.SERVER_ERROR")) {
                mReceiver.onServerError();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.TIMEZONE_ERROR")) {
                mReceiver.onTimezoneError();
            }
        }
    }

    public interface ReceiveListener {
        void onForceLogout();
        void onServerError();
        void onTimezoneError();
    }

    public void setReceiver(ErrorNetworkReceiver.ReceiveListener receiver) {
        this.mReceiver = receiver;
    }
}