package com.tokopedia.notifications.common;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationCacheHandler {

    private final String CACHE_CMNOTIFICATIONS = "cache_cmnotifications";
    private LocalCacheHandler localCacheHandler;

    public CMNotificationCacheHandler(Context context) {
        localCacheHandler = new LocalCacheHandler(context, CACHE_CMNOTIFICATIONS);
    }

    public String getStringValue(String Key) {

        return localCacheHandler.getString(Key);
    }

    public void saveStringValue(String key, String value) {
        localCacheHandler.putString(key, value);
        localCacheHandler.applyEditor();
    }

    public int getIntValue(String Key) {
        return localCacheHandler.getInt(Key);
    }

    public void saveIntValue(String key, int value) {
        localCacheHandler.putInt(key, value);
        localCacheHandler.applyEditor();
    }

}
