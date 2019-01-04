package com.tokopedia.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.notifications.common.CMConstant;

import java.util.Map;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class CMPushNotificationManager {

    private final String TAG = CMPushNotificationManager.class.getCanonicalName();
    private static final CMPushNotificationManager sInstance;
    private Context mContext;

    public static CMPushNotificationManager getInstance() {
        return sInstance;
    }

    static {
        sInstance = new CMPushNotificationManager();
    }

    /**
     * initialization of push notification library
     *
     * @param context
     */
    public void init(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        this.mContext = context.getApplicationContext();
    }

    public Context getApplicationContext() {
        return mContext;
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    public void setFcmTokenCMNotif(String token) {
        CommonUtils.dumper("token: " + token);
        if (mContext == null) {
            return;
        }
        if (TextUtils.isEmpty(token)) {
            return;
        }
        (new CMUserHandler(mContext)).updateToken(token);
    }


    /**
     * To check weather the incoming notification belong to campaign management
     *
     * @param extras
     * @return
     */
    public boolean isFromCMNotificationPlatform(Map<String, String> extras) {
        if (mContext == null) {
            throw new IllegalArgumentException("Kindly invoke init before calling notification library");
        }
        try {
            if (null != extras && extras.containsKey(CMConstant.PayloadKeys.SOURCE)) {
                String confirmationValue =
                        extras.get(CMConstant.PayloadKeys.SOURCE);
                return confirmationValue.equals(CMConstant.PayloadKeys.FCM_EXTRA_CONFIRMATION_VALUE);
            }
        } catch (Exception e) {
            Log.e(TAG, "CMPushNotificationManager: isFromCMNotificationPlatform",e);
        }
        return false;
    }

    /**
     * Handle the remote message and show push notification
     *
     * @param remoteMessage
     */
    public void handlePushPayload(RemoteMessage remoteMessage) {

    }

    private Bundle convertMapToBundle(Map<String, String> map) {
        Bundle bundle = new Bundle(map != null ? map.size() : 0);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

}
