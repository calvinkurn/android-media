package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

object CacheManager {

    private const val PREF_PN_TROUBLESHOOT = "Push_Notification_Troubleshoot"
    private const val KEY_PREF_DATE = "Date"

    fun saveLastCheckedDate(context: Context?) {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        cache.putLong(KEY_PREF_DATE, System.currentTimeMillis())
        cache.applyEditor()
    }

    fun getLastCheckedDate(context: Context?): Long {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        return cache.getLong(KEY_PREF_DATE)
    }

}