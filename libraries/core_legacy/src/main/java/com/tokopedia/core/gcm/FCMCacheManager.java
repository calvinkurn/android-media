package com.tokopedia.core.gcm;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.user.session.UserSession;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author by Herdi_WORK on 13.12.16.
 */

public class FCMCacheManager {
    public static final String GCM_ID_TIMESTAMP = "gcm_id_timestamp";
    public static final long GCM_ID_EXPIRED_TIME = TimeUnit.DAYS.toMillis(3);
    private final String NOTIFICATION_CODE = "tkp_code";
    private static final String GCM_STORAGE = "GCM_STORAGE";
    public static final String SETTING_NOTIFICATION_VIBRATE = "notifications_new_message_vibrate";
    private LocalCacheHandler cache;
    private Context context;

    public static void storeRegId(String id, Context context) {
        new UserSession(context).setDeviceId(id);
    }

    public static void storeFcmTimestamp(Context context) {
        UserSession userSession = new UserSession(context);
        userSession.setFcmTimestamp();
    }

    public static String getRegistrationId(Context context) {
        return new UserSession(context).getDeviceId();
    }

    public static String getRegistrationIdWithTemp(Context context) {
        UserSession userSession = new UserSession(context);
        String deviceId = userSession.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String tempID = getTempFcmId();
            userSession.setDeviceId(tempID);
            return tempID;
        }
        return deviceId;
    }

    private static String getTempFcmId() {
        return UUID.randomUUID().toString();
    }
}