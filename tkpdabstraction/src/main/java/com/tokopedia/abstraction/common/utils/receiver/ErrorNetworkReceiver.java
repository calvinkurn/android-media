package com.tokopedia.abstraction.common.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by ricoharisin on 7/26/16.
 */
public class ErrorNetworkReceiver extends BroadcastReceiver {

    private ReceiveListener mReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null && intent != null) {
            String action = intent.getAction();
            Timber.w("P1#BROADCAST_RECEIVER#ErrorNetworkReceiver;action='%s'", action);
            if (action == null) {
                return;
            }
            if (action.equals("com.tokopedia.tkpd.FORCE_LOGOUT")) {
                mReceiver.onForceLogout();
            } else if (action.equals("com.tokopedia.tkpd.SERVER_ERROR")) {
                mReceiver.onServerError();
            } else if (action.equals("com.tokopedia.tkpd.TIMEZONE_ERROR")) {
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
