package com.tokopedia.profilecompletion.common

import android.content.Context
import android.content.SharedPreferences

class ValidateToken constructor(val context: Context) {

    private fun getSharedPReference() :SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun get(): String? {
        return getSharedPReference().getString(KEY_VALIDATE_TOKEN, "")
    }

    fun set(value: String) {
        getSharedPReference().edit().putString(KEY_VALIDATE_TOKEN, value).apply()
    }

    companion object {
        private const val PREF_NAME = "validateToken.pref"
        private const val KEY_VALIDATE_TOKEN = "validateToken"
    }
}