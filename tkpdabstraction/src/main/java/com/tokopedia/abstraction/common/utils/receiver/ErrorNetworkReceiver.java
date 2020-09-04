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
    private static final String ACCESS_TOKEN = "accessToken";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null && intent != null) {
            String action = intent.getAction();
            String accessToken = "";
            if(intent.getStringExtra(ACCESS_TOKEN) != null) {
                accessToken = intent.getStringExtra(ACCESS_TOKEN);
            }
            Timber.w("P1#BROADCAST_RECEIVER#ErrorNetworkReceiver;action='%s';accessToken='%s", action, accessToken);
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
