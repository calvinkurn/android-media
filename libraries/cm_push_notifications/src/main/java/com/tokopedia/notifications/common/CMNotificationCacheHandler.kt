package com.tokopedia.notifications.common

import android.content.Context

import com.tokopedia.abstraction.common.utils.LocalCacheHandler

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
class CMNotificationCacheHandler(context: Context) {

    private val CACHE_CMNOTIFICATIONS = "cache_cmnotifications"
    private val localCacheHandler: LocalCacheHandler

    init {
        localCacheHandler = LocalCacheHandler(context, CACHE_CMNOTIFICATIONS)
    }

    fun getStringValue(Key: String): String? {
        return localCacheHandler.getString(Key)
    }

    fun saveStringValue(key: String, value: String) {
        localCacheHandler.putString(key, value)
        localCacheHandler.applyEditor()
    }

    fun getIntValue(Key: String): Int {
        return localCacheHandler.getInt(Key)!!
    }

    fun saveIntValue(key: String, value: Int) {
        localCacheHandler.putInt(key, value)
        localCacheHandler.applyEditor()
    }

    fun getLongValue(Key: String): Long {
        return localCacheHandler.getLong(Key)!!
    }

    fun saveLongValue(key: String, value: Long) {
        localCacheHandler.putLong(key, value)
        localCacheHandler.applyEditor()
    }

    fun remove(key: String) {
        localCacheHandler.remove(key)
        localCacheHandler.applyEditor()
    }

}
