package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class TetraDataSource(context: Context) {

    private val STATUS: String = "status"
    private val USER_ID: String = "userId"

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putUserId(value: String) {
        preferences.edit().putString(USER_ID, value).apply()
    }

    fun getUserId(): String {
        return preferences.getString(USER_ID, "")?: ""
    }

    fun putStatus(status: Boolean) {
        preferences.edit().putBoolean(STATUS, status).apply()
    }

    fun isWhitelisted(): Boolean {
        return preferences.getBoolean(STATUS, false) // set true for debugging
    }
}