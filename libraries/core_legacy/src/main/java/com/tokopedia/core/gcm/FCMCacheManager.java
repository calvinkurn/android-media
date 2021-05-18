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

    public boolean isAllowToHandleNotif(Bundle data) {
        try {
            return (!cache.isExpired() || cache.getString(TkpdCache.Key.PREV_CODE) == null ||
                    !data.isEmpty() || data.getString("g_id", "").equals(cache.getString(TkpdCache.Key.PREV_CODE)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    public boolean checkLocalNotificationAppSettings(int code) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        switch (code) {
            case TkpdState.GCMServiceState.GCM_MESSAGE:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_PM, true);
            case TkpdState.GCMServiceState.GCM_TALK:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_TALK, true);

            case TkpdState.GCMServiceState.GCM_REVIEW:
            case TkpdState.GCMServiceState.GCM_REVIEW_EDIT:
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_REVIEW, true);

            case TkpdState.GCMServiceState.GCM_PROMO:
            case TkpdState.GCMServiceState.GCM_HOT_LIST:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);

            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_REP, true);

            case TkpdState.GCMServiceState.GCM_NEWORDER:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_SALES, true);

            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_PURCHASE, true);

            case TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE:
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_RESCENTER, true);

            case TkpdState.GCMServiceState.GCM_SELLER_INFO:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_SELLER_INFO, true);

            case TkpdState.GCMServiceState.GCM_GROUP_CHAT:
            case TkpdState.GCMServiceState.GCM_GROUP_CHAT_POINTS:
            case TkpdState.GCMServiceState.GCM_GROUP_CHAT_LOYALTY:
            case TkpdState.GCMServiceState.GCM_GROUP_CHAT_COUPON:
                return settings.getBoolean(Constants.Settings.NOTIFICATION_GROUP_CHAT, false);
            default:
                return true;
        }
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