package com.tokopedia.expresscheckout.view.variant.util

import android.app.Activity
import android.content.Context.MODE_PRIVATE


/**
 * Created by Irfan Khoirul on 30/01/19.
 */

val SHARED_PREFERENCE_NAME = "express_checkout_onboarding"
val VALUE_NAME_IS_FIRST_TIME_SHOW = "is_first_time_show"

fun isOnboardingStateHasNotShown(activity: Activity?): Boolean {
    return activity?.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
            ?.getBoolean(VALUE_NAME_IS_FIRST_TIME_SHOW, true) ?: false
}

fun setOnboardingStateHasNotShown(activity: Activity?, hasShown: Boolean) {
    activity?.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
            ?.edit()
            ?.putBoolean(VALUE_NAME_IS_FIRST_TIME_SHOW, hasShown)
            ?.apply()
}