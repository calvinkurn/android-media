package com.tokopedia.fcmcommon.common

import android.content.Context

import com.tokopedia.abstraction.common.utils.LocalCacheHandler


class FcmCacheHandler(context: Context) {

    private val CACHE_CMNOTIFICATIONS = "cache_fcmnotifications"
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

    fun remove(key: String) {
        localCacheHandler.remove(key)
        localCacheHandler.applyEditor()
    }

}
