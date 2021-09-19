package com.tokopedia.tokopedianow.common.util

import android.app.Activity
import android.content.Context
import com.tokopedia.kotlin.extensions.orFalse

object SharedPreferencesUtil {

    private const val SHARING_EDUCATION_REMOVED = "SHARING_EDUCATION_REMOVED"
    private const val EDUCATIONAL_INFORMATION_LOTTI_STOPPED = "EDUCATIONAL_INFORMATION_LOTTIE_STOPPED"

    fun isSharingEducationRemoved(activity: Activity?): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getBoolean(SHARING_EDUCATION_REMOVED, false).orFalse()
    }

    fun isEducationalInformationStopped(activity: Activity?): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getBoolean(EDUCATIONAL_INFORMATION_LOTTI_STOPPED, false).orFalse()
    }

    fun setSharingEducationState(activity: Activity?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.run {
            putBoolean(SHARING_EDUCATION_REMOVED, true)
        }?.apply()
    }

    fun setEducationalInformationState(activity: Activity?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.run {
            putBoolean(EDUCATIONAL_INFORMATION_LOTTI_STOPPED, true)
        }?.apply()
    }
}