package com.tokopedia.fcmcommon.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class FcmCacheHandler(
    context: Context
) : LocalCacheHandler(context, CACHE_CM_NOTIFICATIONS) {

    fun getStringValue(key: String): String? {
        return getString(key)
    }

    fun saveStringValue(key: String, value: String) {
        putString(key, value)
        applyEditor()
    }

    fun removeString(key: String) {
        remove(key)
        applyEditor()
    }

    companion object {
        private const val CACHE_CM_NOTIFICATIONS = "cache_fcmnotifications"
    }
}
