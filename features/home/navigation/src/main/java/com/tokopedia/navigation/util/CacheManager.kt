package com.tokopedia.navigation.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class CacheManager @Inject constructor(val context: Context) {

    private fun pref(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun entry(key: String, value: Int) {
        pref().edit().putInt(key, value).apply()
    }

    fun read(key: String): Int {
        return pref().getInt(key, -1)
    }

    fun isExist(key: String): Boolean {
        return pref().contains(key)
    }

}