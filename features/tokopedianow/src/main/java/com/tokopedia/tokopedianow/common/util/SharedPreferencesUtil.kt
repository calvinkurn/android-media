package com.tokopedia.tokopedianow.common.util

import android.app.Activity
import android.content.Context

object SharedPreferencesUtil {

    private const val SHARING_EDUCATION_REMOVED = "SHARING_EDUCATION_REMOVED"

    fun isSharingEducationRemoved(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(SHARING_EDUCATION_REMOVED, false)
    }

    fun setSharingEducationState(activity: Activity) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.run {
            putBoolean(SHARING_EDUCATION_REMOVED, true)
        }?.apply()
    }
}