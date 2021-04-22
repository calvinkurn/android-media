package com.tokopedia.troubleshooter.notification.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

/**
 * duplicated with [com.tokopedia.settingnotif.usersetting.util.CacheManager]
 * to handling last check message (storing the current date and time)
 */
object CacheManager {

    private const val PREF_PN_TROUBLESHOOT = "Push_Notification_Troubleshoot"
    private const val KEY_PREF_DATE = "Date"

    fun saveLastCheckedDate(context: Context?) {
        val cache = LocalCacheHandler(context, PREF_PN_TROUBLESHOOT)
        cache.putLong(KEY_PREF_DATE, System.currentTimeMillis())
        cache.applyEditor()
    }

}