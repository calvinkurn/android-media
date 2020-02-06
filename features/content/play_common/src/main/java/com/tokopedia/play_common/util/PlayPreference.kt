package com.tokopedia.play_common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by jegul on 17/01/20
 */
class PlayPreference @Inject constructor(
        @ApplicationContext context: Context
) {

    companion object {
        private const val PLAY_PREFERENCE = "play_preference"

        private const val FORMAT_ONE_TAP_ONBOARDING = "one_tap_onboarding_%s"
    }

    private val sharedPref = context.getSharedPreferences(PLAY_PREFERENCE, Context.MODE_PRIVATE)

    fun setOneTapOnboardingShown(tag: String) {
        sharedPref.edit().putBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), true).apply()
    }

    fun isOneTapOnboardingShown(tag: String): Boolean {
        return sharedPref.getBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), false)
    }
}