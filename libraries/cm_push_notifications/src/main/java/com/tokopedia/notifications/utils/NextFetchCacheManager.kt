package com.tokopedia.notifications.utils

import android.content.Context

class NextFetchCacheManager constructor(val context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(AMPLIFICATION_PREF, Context.MODE_PRIVATE)
    }

    fun saveNextFetch(value: Long) {
        preferences.edit().putLong(KEY_INTERVAL_FETCH, value).apply()
    }

    fun getNextFetch(): Long {
        return preferences.getLong(KEY_INTERVAL_FETCH, 0)
    }

    companion object {
        private const val AMPLIFICATION_PREF = "amplification_pref"
        private const val KEY_INTERVAL_FETCH = "key_amplification_interval"
    }

}