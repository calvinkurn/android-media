package com.tokopedia.libra.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.backgroundCommit

class PreferencesDelegate(
    val preferences: SharedPreferences,
    val gson: Gson
) {

    inline fun <reified R> get(key: String, default: R): R {
        val pref = preferences.getString(key, "") ?: return default

        return invokeSafe(default) {
            gson.fromJson(pref, R::class.java)
        }
    }

    inline fun <reified R> set(key: String, data: R) {
        val editor = preferences.edit()
        editor.putString(key, gson.toJson(data))

        if (shouldEnableBackgroundCommit()) {
            editor.backgroundCommit()
        } else {
            editor.apply()
        }
    }

    fun clear(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun removeAll() {
        preferences.edit().clear().apply()
    }

    fun shouldEnableBackgroundCommit() = true
}
