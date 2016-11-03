package com.tokopedia.tkpd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ricoharisin on 7/26/16.
 */
public class ErrorNetworkReceiver extends BroadcastReceiver {

    private static ErrorNetworkReceiver.ReceiveListener mReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null) {
            if (intent.getAction().equals("com.tokopedia.tkpd.FORCE_LOGOUT")) {
                mReceiver.onForceLogout();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.SERVER_ERROR")) {
                mReceiver.onServerError();
            }
        }
    }

    public interface ReceiveListener {
        void onForceLogout();
        void onServerError();
    }

    public void setReceiver(ErrorNetworkReceiver.ReceiveListener mReceiver) {
        this.mReceiver = mReceiver;
    }
}
