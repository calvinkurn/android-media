package com.tokopedia.notifications.common;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;

import java.io.IOException;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationUtils {

    public static boolean tokenUpdateRequired(Context context, String newToken) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldToken = cacheHandler.getStringValue(context, CMConstant.FCM_TOKEN_CACHE_KEY);
        if (TextUtils.isEmpty(oldToken)) {
            return true;

        } else if (oldToken.equals(newToken)) {
            return false;
        }
        return true;
    }

    public static boolean mapTokenWithUserRequired(Context context, String newUserId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldUserID = cacheHandler.getStringValue(context, CMConstant.USERID_CACHE_KEY);
        if (TextUtils.isEmpty(newUserId)) {
            return false;

        } else if (newUserId.equals(oldUserID)) {
            return false;
        }
        return true;
    }


    public static boolean mapTokenWithGAdsIdRequired(Context context, String gAdsId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldGAdsId = cacheHandler.getStringValue(context, CMConstant.GADSID_CACHE_KEY);
        if (TextUtils.isEmpty(gAdsId)) {
            return false;

        } else if (gAdsId.equals(oldGAdsId)) {
            return false;
        }
        return true;
    }

    public static void saveToken(Context context, String token) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        cacheHandler.saveStringValue(context, CMConstant.FCM_TOKEN_CACHE_KEY, token);
    }

    public static void saveUserId(Context context, String userId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        cacheHandler.saveStringValue(context, CMConstant.USERID_CACHE_KEY, userId);
    }

    public static void saveGAdsIdId(Context context, String gAdsId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        cacheHandler.saveStringValue(context, CMConstant.GADSID_CACHE_KEY, gAdsId);
    }

    public static String getCurrentLocalTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}
