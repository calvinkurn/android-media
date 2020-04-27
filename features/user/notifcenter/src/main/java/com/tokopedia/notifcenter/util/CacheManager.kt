package com.tokopedia.notifcenter.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class CacheManager @Inject constructor(val context: Context) {

    private fun pref(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val privatePref: SharedPreferences by lazy {
        context.getSharedPreferences(NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)
    }

    var isDisplayedGimmick: Boolean
        get() = privatePref.getBoolean(KEY_IS_DISPLAYED_GIMMICK, false)
        set(value) = privatePref.edit().putBoolean(KEY_IS_DISPLAYED_GIMMICK, value).apply()

    fun entry(key: String, value: Int) {
        pref().edit().putInt(key, value).apply()
    }

    fun read(key: String): Int {
        return pref().getInt(key, -1)
    }

    fun isExist(key: String): Boolean {
        return pref().contains(key)
    }

    companion object {
        private const val NOTIFICATION_PREFERENCE = "notif_preference"
        private const val KEY_IS_DISPLAYED_GIMMICK = "key_is_displayed_gimmick_notif"
    }

}