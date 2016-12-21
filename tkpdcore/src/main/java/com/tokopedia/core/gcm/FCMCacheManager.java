package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.prototype.ManageProductCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;

/**
 * @author  by Herdi_WORK on 13.12.16.
 */

public class FCMCacheManager {
    private String NOTIFICATION_CODE = "tkp_code";
    private static final String GCM_STORAGE = "GCM_STORAGE";
    LocalCacheHandler cache;
    Context context;

    public FCMCacheManager(Context ctx){
        this(ctx, TkpdCache.G_CODE);
        context = ctx;
    }

    public FCMCacheManager(Context ctx, String cacheCode){
        cache = new LocalCacheHandler(ctx, cacheCode);
        context = ctx;
    }

    public void setCache(Context ctx){
        if(cache==null)
            cache = new LocalCacheHandler(ctx, TkpdCache.G_CODE);

        cache.setExpire(1);
        cache.applyEditor();
    }

    void resetCache(Bundle data) {
        if (Integer.parseInt(data.getString(NOTIFICATION_CODE)) > 600
                && Integer.parseInt(data.getString(NOTIFICATION_CODE)) < 802) {
            doResetCache(Integer.parseInt(data.getString(NOTIFICATION_CODE)));
        }
    }

    void updateStats(Bundle data){
        LocalCacheHandler updateStats = new LocalCacheHandler(context, TkpdCache.STATUS_UPDATE);
        updateStats.putInt(TkpdCache.Key.STATUS, Integer.parseInt(data.getString("status")));
        updateStats.applyEditor();
    }

    public void doResetCache(int code) {
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
            return (!cache.isExpired() || cache.getString(TkpdCache.Key.PREV_CODE) == null || !data.isEmpty() || data.getString("g_id").equals(cache.getString(TkpdCache.Key.PREV_CODE)));
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

    public boolean checkSettings(int code) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        switch (code) {
            case TkpdState.GCMServiceState.GCM_MESSAGE:
                return settings.getBoolean("notification_receive_pm", true);
            case TkpdState.GCMServiceState.GCM_TALK:
                return settings.getBoolean("notification_receive_talk", true);
            case TkpdState.GCMServiceState.GCM_REVIEW:
                return settings.getBoolean("notification_receive_review", true);
            case TkpdState.GCMServiceState.GCM_REVIEW_EDIT:
                return settings.getBoolean("notification_receive_review", true);
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                return settings.getBoolean("notification_receive_review", true);
            case TkpdState.GCMServiceState.GCM_PROMO:
                return settings.getBoolean("notification_receive_promo", true);
            case TkpdState.GCMServiceState.GCM_HOT_LIST:
                return settings.getBoolean("notification_receive_promo", true);
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
                return settings.getBoolean("notification_receive_reputation", true);
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
                return settings.getBoolean("notification_receive_reputation", true);
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
                return settings.getBoolean("notification_receive_reputation", true);
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                return settings.getBoolean("notification_receive_reputation", true);
            case TkpdState.GCMServiceState.GCM_NEWORDER:
                return settings.getBoolean("notification_sales", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
                return settings.getBoolean("notification_purchase", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
                return settings.getBoolean("notification_purchase", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
                return settings.getBoolean("notification_purchase", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
                return settings.getBoolean("notification_purchase", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                return settings.getBoolean("notification_purchase", true);
            case TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
                return settings.getBoolean("notification_receive_rescenter", true);
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                return settings.getBoolean("notification_receive_rescenter", true);
        }
        return true;
    }

    public void processNotifData(Bundle data, String title, String descString, CacheProcessListener listener){

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

        listener.onDataProcessed(content,desc,code);
    }

    interface CacheProcessListener{
        void onDataProcessed(ArrayList<String> content,
                ArrayList<String> desc,
                ArrayList<Integer> code);
    }

    public static void storeRegId(String id, Context context){
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString("gcm_id", id);
        cache.applyEditor();
    }

    public static String getRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        return cache.getString("gcm_id", "");
    }

    public static void clearRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString("gcm_id", null);
        cache.applyEditor();
    }

    public static void storeGCMRegId(String id, Context context){
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString("gcm_id_loca", id);
        cache.applyEditor();
    }


    public static String getGCMRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        return cache.getString("gcm_id_loca", "");
    }

    public static void clearGCMRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        cache.putString("gcm_id_loca", null);
        cache.applyEditor();
    }
}
