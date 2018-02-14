package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.gcm.data.entity.NotificationEntity;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.prototype.ManageProductCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author by Herdi_WORK on 13.12.16.
 */

public class FCMCacheManager {
    public static final String GCM_ID = "gcm_id";
    public static final String GCM_ID_TIMESTAMP = "gcm_id_timestamp";
    public static final long GCM_ID_EXPIRED_TIME = TimeUnit.DAYS.toMillis(3);
    private String NOTIFICATION_CODE = "tkp_code";
    private static final String GCM_STORAGE = "GCM_STORAGE";
    private static final String NOTIFICATION_STORAGE = "NOTIFICATION_STORAGE";
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

    public void setCache(Context ctx) {
        if (cache == null)
            cache = new LocalCacheHandler(ctx, TkpdCache.G_CODE);

        cache.setExpire(1);
        cache.applyEditor();
    }

    public void setCache() {
        if (cache == null)
            cache = new LocalCacheHandler(context, TkpdCache.G_CODE);

        cache.setExpire(1);
        cache.applyEditor();
    }

    public void resetCache(Bundle data) {
        if (Integer.parseInt(data.getString(NOTIFICATION_CODE)) > 600
                && Integer.parseInt(data.getString(NOTIFICATION_CODE)) < 802) {
            doResetCache(Integer.parseInt(data.getString(NOTIFICATION_CODE)));
        }
    }

    void updateUpdateAppStatus(Bundle data) {
        LocalCacheHandler updateStats = new LocalCacheHandler(context, TkpdCache.STATUS_UPDATE);
        updateStats.putInt(TkpdCache.Key.STATUS, Integer.parseInt(data.getString("status")));
        updateStats.applyEditor();
    }

    private void doResetCache(int code) {
        switch (code) {
            case TkpdState.GCMServiceState.GCM_PEOPLE_PROFILE:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_PROFILE, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_PEOPLE_NOTIF_SETTING:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_NOTIFICATION, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_PEOPLE_PRIVACY_SETTING:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_PRIVACY, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_PEOPLE_ADDRESS_SETTING:

                break;
            case TkpdState.GCMServiceState.GCM_SHOP_INFO:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_SHOP_PAYMENT:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_PAYMENT, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_SHOP_ETALASE:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ETALASE, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_SHOP_NOTES:
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_NOTES, context.getApplicationContext());
                break;
            case TkpdState.GCMServiceState.GCM_PRODUCT_LIST:
                ManageProductCache.ClearCache(context.getApplicationContext());
                break;
        }
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
        CommonUtils.dumper("prev time: " + prevTime);
        CommonUtils.dumper("curr time: " + currTIme);
        if (currTIme - prevTime > 15000) {
            cache.putLong(TkpdCache.Key.PREV_TIME, currTIme);
            cache.applyEditor();
            return true;
        }
        return false;
    }

    public Boolean isVibrate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("notifications_new_message_vibrate", false);
    }

    public Uri getSoundUri() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String StringSoundUri = settings.getString("notifications_new_message_ringtone", null);
        if (StringSoundUri != null) {
            return Uri.parse(StringSoundUri);
        } else {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
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

            default:
                return true;
        }
    }

    void processNotifData(Bundle data, String title, String descString, CacheProcessListener listener) {

        ArrayList<String> content, desc;
        ArrayList<Integer> code;

        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.GCM_NOTIFICATION);
        content = cache.getArrayListString(TkpdCache.Key.NOTIFICATION_CONTENT);
        desc = cache.getArrayListString(TkpdCache.Key.NOTIFICATION_DESC);
        code = cache.getArrayListInteger(TkpdCache.Key.NOTIFICATION_CODE);
        try {
            for (int i = 0; i < code.size(); i++) {
                if (code.get(i) == Integer.parseInt(data.getString(NOTIFICATION_CODE))) {
                    content.remove(i);
                    code.remove(i);
                    desc.remove(i);
                }
            }
        } catch (Exception e) {
            Crashlytics.log(Log.ERROR, "PUSH NOTIF - IndexOutOfBounds",
                    "tkp_code:" + Integer.parseInt(data.getString(NOTIFICATION_CODE)) +
                            " size contentArray " + content.size() +
                            " size codeArray " + code.size() +
                            " size Desc " + desc.size());
            e.printStackTrace();
        }

        content.add(title);
        code.add(Integer.parseInt(data.getString(NOTIFICATION_CODE)));
        desc.add(descString);

        cache.putArrayListString(TkpdCache.Key.NOTIFICATION_CONTENT, content);
        cache.putArrayListString(TkpdCache.Key.NOTIFICATION_DESC, desc);
        cache.putArrayListInteger(TkpdCache.Key.NOTIFICATION_CODE, code);
        cache.applyEditor();

        listener.onDataProcessed(content, desc, code);
    }

    interface CacheProcessListener {
        void onDataProcessed(ArrayList<String> content,
                             ArrayList<String> desc,
                             ArrayList<Integer> code);
    }

    public static void storeRegId(String id, Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString(GCM_ID, id);
        cache.applyEditor();
    }

    public static void storeFcmTimestamp(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putLong(GCM_ID_TIMESTAMP, System.currentTimeMillis());
        cache.applyEditor();
    }

    public static boolean isFcmExpired(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        long lastFCMUpdate = cache.getLong(GCM_ID_TIMESTAMP, 0);
        if (lastFCMUpdate <= 0) {
            FCMCacheManager.storeFcmTimestamp(context);
            return false;
        }

        return (System.currentTimeMillis() - lastFCMUpdate) >= GCM_ID_EXPIRED_TIME;
    }

    public static void checkAndSyncFcmId(final Context context) {
        if (FCMCacheManager.isFcmExpired(context)) {
            SessionHandler sessionHandler = new SessionHandler(context);
            if (sessionHandler.isV4Login()) {
                IFCMTokenReceiver fcmRefreshTokenReceiver = new FCMTokenReceiver(context);
                FCMTokenUpdate tokenUpdate = new FCMTokenUpdate();
                tokenUpdate.setNewToken(FCMCacheManager.getRegistrationId(context));
                tokenUpdate.setOsType(String.valueOf(1));
                tokenUpdate.setAccessToken(sessionHandler.getAccessToken(context));
                tokenUpdate.setUserId(sessionHandler.getLoginID());
                fcmRefreshTokenReceiver.onTokenReceive(Observable.just(tokenUpdate));
            }
        }
    }

    public static String getRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        return cache.getString(GCM_ID, "");
    }

    public String getRegistrationId() {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        return cache.getString(GCM_ID, "");
    }

    public static void setDialogNotificationSetting(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        cache.putBoolean("notification_dialog", true);
        cache.applyEditor();
    }

    public static boolean isDialogNotificationSettingShowed(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        return cache.getBoolean("notification_dialog", false);
    }

    public static void clearDialogNotificationSetting(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        cache.putBoolean("notification_dialog", false);
        cache.applyEditor();
    }

    public static String getRegistrationIdWithTemp(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        if (cache.getString("gcm_id", "").equals("")) {
            String tempID = getTempFcmId();
            cache.putString("gcm_id", tempID);
            cache.applyEditor();
            return tempID;
        }
        return cache.getString("gcm_id", "");
    }

    static void clearRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString("gcm_id", null);
        cache.applyEditor();
    }

    public void saveIncomingNotification(NotificationEntity notificationEntity) {
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

    public List<NotificationEntity> getHistoryPushNotification() {
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
