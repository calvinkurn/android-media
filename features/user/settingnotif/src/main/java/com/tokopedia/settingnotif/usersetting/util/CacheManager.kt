package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import android.preference.PreferenceManager.getDefaultSharedPreferences as sharedPreferences

class CacheManager(context: Context?) {

    val preferences: SharedPreferences? by lazy(LazyThreadSafetyMode.NONE) {
        sharedPreferences(context)
    }

    inline fun <reified T> entry(key: String, obj: T) {
        val objString = Gson().toJson(obj, T::class.java)
        preferences?.edit()?.putString(key, objString)
    }

    inline fun <reified T> read(key: String): T? {
        val obj = preferences?.getString(key, null)
        return Gson().fromJson(obj, T::class.java)
    }

    companion object {
        const val KEY_TEMP_SETTING = "temp_setting"
    }

}