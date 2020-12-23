package com.tokopedia.feedback_form.feedbackpage.ui.preference

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context?) {

    val PREFERENCE_NAME = "SharePreferenceEmail"
    val EXTRA_IS_SUBMIT = "EXTRA_IS_SUBMIT"
    val EXTRA_IS_USER_ID = "EXTRA_IS_USER_ID"

    private val sharePreferences: SharedPreferences? by lazy {
        context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    val editor = sharePreferences?.edit()

    fun getSubmitFlag(userId: String) : String? {
        val latestUser = sharePreferences?.getString(EXTRA_IS_USER_ID, "")
        if (userId != latestUser) {
            editor?.clear()?.apply()
        }
        return sharePreferences?.getString(EXTRA_IS_SUBMIT, null)
    }

    fun setSubmitFlag(email: String, userId: String) {
        editor?.putString(EXTRA_IS_SUBMIT, email)
        editor?.putString(EXTRA_IS_USER_ID, userId)
        editor?.apply()
    }
}