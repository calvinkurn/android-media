package com.tokopedia.notifications.common;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationUtils {

    public static boolean needTokenUpdateRequired(Context context, String newToken) {
        CMNotificationCacheHandler cacheHandler = new CMNotificationCacheHandler();
        String oldToken = cacheHandler.getStringValue(context, CMConstant.FCM_TOKEN_CACHE_KEY);
        if (TextUtils.isEmpty(oldToken)) {
            return true;

        } else if (oldToken.equals(newToken)) {
            return false;
        }
        return true;
    }
}
