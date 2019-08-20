package com.tokopedia.analytics.debugger.data.source

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class TetraDataSource(context: Context) {

    private val STATUS: String = "status"

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putStatus(status: Boolean) {
        preferences.edit().putBoolean(STATUS, status).apply()
    }

    fun isWhitelisted(): Boolean {
        return preferences.getBoolean(STATUS, true) // set true for debugging
    }
}