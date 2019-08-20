package com.tokopedia.analytics.debugger.data.source

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class TetraDataSource(context: Context) {

    private val STATUS: String = "status"

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putStatus(status: String) {
        preferences.edit().putBoolean(STATUS, status.contentEquals("true")).apply()
    }

    fun isWhitelisted(): Boolean {
        return preferences.getBoolean(STATUS, false)
    }
}