package com.tokopedia.moengage_wrapper.cache

import android.content.Context
import android.content.SharedPreferences

class MoengageWrapperCacheHandler(context: Context) {

    private var sharedPrefs: SharedPreferences? = null
    private val CACHE_MOENGAGE_WRAPPER = "cache_moengage_wrapper"

    init {
        sharedPrefs = context.getSharedPreferences(CACHE_MOENGAGE_WRAPPER, Context.MODE_PRIVATE)
    }

    fun clearCache() {
        sharedPrefs?.edit()?.clear()?.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPrefs?.run {
            getBoolean(key, false)
        } ?: false
    }


}