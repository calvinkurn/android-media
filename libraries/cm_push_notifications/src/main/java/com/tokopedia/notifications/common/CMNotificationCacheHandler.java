package com.tokopedia.notifications.common;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationCacheHandler {

    private final String CACHE_CMNOTIFICATIONS = "cache_cmnotifications";

    public String getStringValue(Context context, String Key) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CMNOTIFICATIONS);
        return localCacheHandler.getString(Key);
    }

    public void saveStringValue(Context context, String key, String value) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CMNOTIFICATIONS);
        localCacheHandler.putString(key, value);
        localCacheHandler.applyEditor();
    }

    public int getIntValue(Context context, String Key) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CMNOTIFICATIONS);
        return localCacheHandler.getInt(Key);
    }

    public void saveIntValue(Context context, String key, int value) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CMNOTIFICATIONS);
        localCacheHandler.putInt(key, value);
        localCacheHandler.applyEditor();
    }

}
