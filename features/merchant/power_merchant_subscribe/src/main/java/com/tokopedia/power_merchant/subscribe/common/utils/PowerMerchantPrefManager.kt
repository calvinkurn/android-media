package com.tokopedia.power_merchant.subscribe.common.utils

import android.content.Context

class PowerMerchantPrefManager(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "power_merchant_prefs"
        private const val IS_SHOW_COACH_MARK = "isShowCoachMarkPM"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setIsShowCoachMarkPM(isShowPopEndTenure: Boolean) {
        putBoolean(IS_SHOW_COACH_MARK, isShowPopEndTenure)
    }

    fun getFinishCoachMark() = getBoolean(IS_SHOW_COACH_MARK, false)
}