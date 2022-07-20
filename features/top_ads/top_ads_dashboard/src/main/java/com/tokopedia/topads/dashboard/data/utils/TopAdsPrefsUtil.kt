package com.tokopedia.topads.dashboard.data.utils

import android.app.Activity
import android.content.Context

object TopAdsPrefsUtil {

    fun Activity.showBerandaDialog() =
        getPreferences(Context.MODE_PRIVATE).getBoolean(BERANDA_DIALOG, true)

    fun Activity.berandaDialogShown() {
        writeToPref(BERANDA_DIALOG, false)
    }

    private fun Activity.writeToPref(key: String, value: Any) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
            }
            commit()
        }
    }

    private const val START_DATE = "start_date"
    private const val END_DATE = "end_date"
    private const val BERANDA_DIALOG = "beranda_dialog"
}