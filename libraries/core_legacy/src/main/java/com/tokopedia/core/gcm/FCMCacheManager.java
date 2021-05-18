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

    public FCMCacheManager(Context ctx) {
        this(ctx, TkpdCache.G_CODE);
        context = ctx;
    }

    private FCMCacheManager(Context ctx, String cacheCode) {
        cache = new LocalCacheHandler(ctx, cacheCode);
        context = ctx;
    }

    public Boolean isAllowBell() {
        long prevTime = cache.getLong(TkpdCache.Key.PREV_TIME);
        long currTIme = System.currentTimeMillis();
        Timber.d("prev time: " + prevTime);
        Timber.d("curr time: " + currTIme);
        if (currTIme - prevTime > 15000) {
            cache.putLong(TkpdCache.Key.PREV_TIME, currTIme);
            cache.applyEditor();
            return true;
        }
        return false;
    }

    public Boolean isVibrate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(SETTING_NOTIFICATION_VIBRATE, false);
    }

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

    public void saveIncomingNotification(NotificationEntity notificationEntity) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "FCMCacheManager_saveIncomingNotification");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        boolean isExist = false;
        List<NotificationEntity> notificationEntities = getHistoryPushNotification();
        for (int i = 0; i < notificationEntities.size(); i++) {
            if (notificationEntities.get(i).getCode().equalsIgnoreCase(notificationEntity.getCode())) {
                isExist = true;
                notificationEntities.remove(i);
                notificationEntities.add(notificationEntity);
                break;
            }
        }
        if (!isExist) {
            notificationEntities.add(notificationEntity);
        }
        Type baseType = new TypeToken<List<NotificationEntity>>() {
        }.getType();
        Gson gson = new Gson();
        String newList = gson.toJson(notificationEntities, baseType);
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.GCM_NOTIFICATION);
        localCacheHandler.putString(TkpdCache.Key.NOTIFICATION_PASS_DATA, newList);
        localCacheHandler.applyEditor();
    }

    List<NotificationEntity> getHistoryPushNotification() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.GCM_NOTIFICATION);
        List<NotificationEntity> mNotificationEntity =
                convertDataList(
                        NotificationEntity[].class,
                        localCacheHandler.getString(TkpdCache.Key.NOTIFICATION_PASS_DATA, "")
                );
        if (mNotificationEntity != null)
            return new ArrayList<>(mNotificationEntity);
        else
            return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> convertDataList(Class<T[]> clazz, String data) {
        if (TextUtils.isEmpty(data))
            return null;
        Object objData;
        try {
            Gson gson = new Gson();
            objData = Arrays.asList((T[]) gson.fromJson(data, clazz));
            return (List<T>) objData;
        } catch (ClassCastException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getTempFcmId() {
        return UUID.randomUUID().toString();
    }
}