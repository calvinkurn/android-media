package com.tokopedia.sellerfeedback.presentation.util

import android.content.Context
import com.tokopedia.kotlin.extensions.remove

class ScreenshotPreferenceManager(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "seller_feedback_pref"
        private const val SELECTED_PAGE = "seller_feedback_selected_fragment"
        private const val SCREEN_SHOOT_TRIGGER_ENABLED = "screen_shoot_trigger_enabled"
        private const val FEEDBACK_FORM_SAVED_STATUS = "feedback_form_saved_status"
        private const val EMPTY_STRING = ""
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

    fun isFeedbackFormSavedSuccess(): Boolean {
        return getBoolean(FEEDBACK_FORM_SAVED_STATUS, false)
    }

    fun setFeedbackFormSavedStatus(isSuccess: Boolean) {
        putBoolean(FEEDBACK_FORM_SAVED_STATUS, isSuccess)
    }

    fun getSelectedFragment(defVal: String): String {
        return getString(SELECTED_PAGE, defVal)
    }

    private fun getString(key: String, defVal: String = EMPTY_STRING): String {
        return sharedPref.getString(key, defVal) ?: defVal
    }

    private fun getBoolean(key: String, defVal: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defVal)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}