package com.tokopedia.sellerfeedback.presentation.util

import android.content.Context

class ScreenshotPreferenceManage(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "seller_feedback_toaster_pref"
        private const val HAS_DATE_TOASTER = "seller_feedback_date_toaster"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue).orEmpty()
    }

    private fun putString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setDateToaster(value: String) {
        putString(HAS_DATE_TOASTER, value)
    }

    fun getDateToaster() = getString(HAS_DATE_TOASTER)
}