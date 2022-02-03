package com.tokopedia.topads.dashboard.data.utils

import android.app.Activity
import android.content.Context
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant

object TopAdsPrefsUtil {

    fun Activity.showBerandaDialog() =
        getPreferences(Context.MODE_PRIVATE).getBoolean(BERANDA_DIALOG, true)

    fun Activity.berandaDialogShown() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(BERANDA_DIALOG, false)
            commit()
        }
    }

    private const val BERANDA_DIALOG = "beranda_dialog"
}