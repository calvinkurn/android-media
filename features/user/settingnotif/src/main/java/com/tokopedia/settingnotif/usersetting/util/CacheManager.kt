package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

object CacheManager {

    private const val PREF_PN_TROUBLESHOOT = "Push_Notification_Troubleshoot"

    const val KEY_PREF_DATE = "Date"
    const val KEY_ONBOARDING = "Onboarding"

    fun getCacheLong(context: Context?, key: String): Long {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        return cache.getLong(key)
    }

    fun saveCacheBoolean(
            context: Context?,
            key: String,
            value: Boolean
    ) {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        cache.putBoolean(key, value)
        cache.applyEditor()
    }

    fun getCacheBoolean(
            context: Context?,
            key: String
    ): Boolean {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        return cache.getBoolean(key)
    }

}