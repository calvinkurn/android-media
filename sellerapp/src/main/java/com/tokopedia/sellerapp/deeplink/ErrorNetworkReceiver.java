package com.tokopedia.sellerapp.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by ricoharisin on 7/26/16.
 */
public class ErrorNetworkReceiver extends BroadcastReceiver {

    private ReceiveListener mReceiver;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String PATH = "path";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver != null && intent != null) {
            String action = intent.getAction();
            String accessToken = "";
            String path = "";
            if(intent.getStringExtra(ACCESS_TOKEN) != null) {
                accessToken = intent.getStringExtra(ACCESS_TOKEN);
            }
            if(intent.getStringExtra(PATH) != null) {
                path = intent.getStringExtra(PATH);
            }
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "ErrorNetworkReceiver");
            messageMap.put("action", action);
            messageMap.put("accessToken", accessToken);
            messageMap.put("path", path);
            ServerLogger.log(Priority.P1, "BROADCAST_RECEIVER", messageMap);
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