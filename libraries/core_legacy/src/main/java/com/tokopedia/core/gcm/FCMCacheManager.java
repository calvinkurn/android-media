package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.core.gcm.data.entity.NotificationEntity;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

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