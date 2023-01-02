package com.tokopedia.fcmcommon.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class FcmCacheHandler(
    context: Context,
    cacheCMNotifications: String
) : LocalCacheHandler(context, cacheCMNotifications) {

    fun getStringValue(Key: String): String? {
        return getString(Key)
    }

    fun saveStringValue(key: String, value: String) {
        putString(key, value)
        applyEditor()
    }

    fun removeString(key: String) {
        remove(key)
        applyEditor()
    }
}
