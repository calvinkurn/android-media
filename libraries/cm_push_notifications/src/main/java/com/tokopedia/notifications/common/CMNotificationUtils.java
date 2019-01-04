package com.tokopedia.notifications.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.notifications.model.BaseNotificationModel;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationUtils {

    static String TAG = CMNotificationUtils.class.getSimpleName();

    static final String STATE_LOGGED_OUT = "LOGGED_OUT";
    static final String STATE_LOGGED_IN = "LOGGED_IN";


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

    public static String getUserStatus(Context context, String userId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldUserId = cacheHandler.getStringValue(context, CMConstant.USERID_CACHE_KEY);
        if (TextUtils.isEmpty(userId)) {
            if (TextUtils.isEmpty(oldUserId)) {
                return "";
            } else {
                return STATE_LOGGED_OUT;
            }
        } else {
            return STATE_LOGGED_IN;
        }
    }

    public static boolean mapTokenWithUserRequired(Context context, String newUserId) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldUserID = cacheHandler.getStringValue(context, CMConstant.USERID_CACHE_KEY);
        if (TextUtils.isEmpty(oldUserID)) {
            return !TextUtils.isEmpty(newUserId);
        } else if (!TextUtils.isEmpty(oldUserID)) {
            return TextUtils.isEmpty(newUserId);
        } else if (!TextUtils.isEmpty(newUserId)) {
            return !newUserId.equals(oldUserID);
        }
        return false;
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

    public static boolean mapTokenWithAppVersionRequired(Context context, int appVersion) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        int oldAppVersion = cacheHandler.getIntValue(context, CMConstant.APP_VERSION_CACHE_KEY);

        CommonUtils.dumper("CMUser-APP_VERSION" + oldAppVersion + "#new-" + appVersion);
        if (oldAppVersion == appVersion)
            return false;
        return true;
    }

    public static String getUniqueAppId(Context context) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String appId = cacheHandler.getStringValue(context, CMConstant.UNIQUE_APP_ID_CACHE_KEY);
        if (TextUtils.isEmpty(appId)) {
            appId = UUID.randomUUID().toString();
            cacheHandler.saveStringValue(context, CMConstant.UNIQUE_APP_ID_CACHE_KEY, appId);
        }
        return appId;
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

    public static void saveAppVersion(Context context, int version) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        cacheHandler.saveIntValue(context, CMConstant.APP_VERSION_CACHE_KEY, version);
    }

    public static long getCurrentLocalTimeStamp() {
        return System.currentTimeMillis();
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int getCurrentAppVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap loadBitmapFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (OutOfMemoryError e) {
            Log.e(TAG, String.format("Out of Memory Error in image bitmap download for Url: %s.", imageUrl));
        } catch (UnknownHostException e) {
            Log.e(TAG, String.format("Unknown Host Exception in image bitmap download for Url: %s. Device "
                    + "may be offline.", imageUrl));
        } catch (MalformedURLException e) {
            Log.e(TAG, String.format("Malformed URL Exception in image bitmap download for Url: %s. Image "
                    + "Url may be corrupted.", imageUrl));
        } catch (Exception e) {
            Log.e(TAG, String.format("Exception in image bitmap download for Url: %s", imageUrl));
        }
        return bitmap;
    }

    public static boolean hasActionButton(BaseNotificationModel baseNotificationModel) {
        return (baseNotificationModel.getActionButton() != null && baseNotificationModel.getActionButton().size() > 0);

    }


    public static Spanned getSpannedTextFromStr(String str) {
        if (null == str)
            return new SpannableStringBuilder("");
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(str);
            }
        } catch (Exception e) {
            return new SpannableStringBuilder(str);
        }
    }
}
