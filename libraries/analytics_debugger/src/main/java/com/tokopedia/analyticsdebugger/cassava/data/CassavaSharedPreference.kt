package com.tokopedia.analyticsdebugger.cassava.data

import android.content.Context

private const val CASSAVA_PREFERENCE_NAME = "cassava_pref"
private const val KEY_NOTIF_ENABLED = "is_notif_enabled"

class CassavaSharedPreference(context: Context) {

    private val mPref = context.getSharedPreferences(CASSAVA_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setNotifEnabled(enabled: Boolean) {
        mPref.edit().putBoolean(KEY_NOTIF_ENABLED, enabled).apply()
    }

    fun isNotifEnabled(): Boolean {
        return mPref.getBoolean(KEY_NOTIF_ENABLED, false)
    }

}