package com.tokopedia.sellerhomecommon.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by @ilhamsuaib on 21/01/22.
 */

class WidgetLastUpdatedSharedPref(
    private val context: Context
) : WidgetLastUpdatedSharedPrefInterface {

    companion object {
        private const val PREF_NAME = "shc_widget_last_updated_info_pref"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun saveLastUpdateInfo(dataKey: String, timeInMillis: Long) {
        val spe = sharedPref.edit()
        spe.putLong(dataKey, timeInMillis)
        spe.apply()
    }

    override fun getLastUpdateInfoInMillis(dataKey: String, default: Long): Long {
        return sharedPref.getLong(dataKey, default)
    }
}