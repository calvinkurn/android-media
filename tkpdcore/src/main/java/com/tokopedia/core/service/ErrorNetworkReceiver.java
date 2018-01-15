package com.tokopedia.core.service;

/**
 * Created by ricoharisin on 7/26/16.
 */
public class ErrorNetworkReceiver extends BroadcastReceiver {

    private ReceiveListener mReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null) {
            if (intent.getAction().equals("com.tokopedia.tkpd.FORCE_LOGOUT")) {
                mReceiver.onForceLogout();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.SERVER_ERROR")) {
                mReceiver.onServerError();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.TIMEZONE_ERROR")) {
                mReceiver.onTimezoneError();
            } else if (intent.getAction().equals("com.tokopedia.tkpd.FORCE_HOCKEYAPP")) {
                mReceiver.onForceHockeyApp();
            }
        }
    }

    public interface ReceiveListener {
        void onForceLogout();
        void onServerError();
        void onTimezoneError();
        void onForceHockeyApp();
    }

    public void setReceiver(ErrorNetworkReceiver.ReceiveListener receiver) {
        this.mReceiver = receiver;
    }


}