package com.tokopedia.sellerfeedback.presentation.util

import android.content.Context

class ScreenshotPreferenceManager(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "seller_feedback_toaster_pref"
        private const val HAS_DATE_TOASTER = "seller_feedback_date_toaster"
        private const val SCREEN_SHOOT_TRIGGER_ENABLED = "screen_shoot_trigger_enabled"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setScreenShootTriggerEnabled(isEnabled: Boolean) {
        putBoolean(SCREEN_SHOOT_TRIGGER_ENABLED, isEnabled)
    }

    fun isScreenShootTriggerEnabled(): Boolean {
        return getBoolean(SCREEN_SHOOT_TRIGGER_ENABLED, false)
    }

    fun setDateToaster(value: String) {
        putString(HAS_DATE_TOASTER, value)
    }

    fun getDateToaster() = getString(HAS_DATE_TOASTER)

    private fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue).orEmpty()
    }

    private fun getBoolean(key: String, defVal: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defVal)
    }

    private fun putString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}