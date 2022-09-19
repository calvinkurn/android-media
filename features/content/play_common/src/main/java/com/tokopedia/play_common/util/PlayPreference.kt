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

        val DAY = 86400000L

        private const val FORMAT_ONE_TAP_ONBOARDING = "one_tap_onboarding_%s"
        private const val FORMAT_SWIPE_ONBOARDING = "swipe_onboarding_%s"
        private const val SWIPE_ONBOARDING = "new_swipe_onboarding_%s"
    }

    private val sharedPref = context.getSharedPreferences(PLAY_PREFERENCE, Context.MODE_PRIVATE)

    fun setOnboardingShown(tag: String) {
        setSwipeOnboardingShown(tag)
    }

    fun isOnboardingShown(tag: String): Boolean {
        return isSwipeOnboardingShown(tag)
    }

    /**
     * Valid from Initial Play -> Swipe Room
     */
    private fun setOneTapOnboardingShown(tag: String) {
        sharedPref.edit().putBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), true).apply()
    }

    /**
     * Valid from Initial Play -> Swipe Room
     */
    private fun isOneTapOnboardingShown(tag: String): Boolean {
        return sharedPref.getBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), false)
    }

    /**
     * Valid since Swipe Room
     */
    private fun setSwipeOnboardingShown(tag: String) {
        sharedPref.edit().putBoolean(String.format(FORMAT_SWIPE_ONBOARDING, tag), true).apply()
    }

    /**
     * Valid since Swipe Room
     */
    private fun isSwipeOnboardingShown(tag: String): Boolean {
        return sharedPref.getBoolean(String.format(FORMAT_SWIPE_ONBOARDING, tag), false)
    }

    /**
     * check last visit
     */
    private var countDay: Long = 0L
        set(value) {
            field = value
        }
        get() {
            val current = System.currentTimeMillis()
            return current - field
        }

    fun setCoachMark() {
        countDay = sharedPref.getLong(SWIPE_ONBOARDING, 0L)
        if (countDay >= DAY) sharedPref.edit().putLong(SWIPE_ONBOARDING, System.currentTimeMillis()).apply()
    }

    fun isCoachMark(): Boolean {
        countDay = sharedPref.getLong(SWIPE_ONBOARDING, 0L)
        return countDay >= DAY
    }
}
