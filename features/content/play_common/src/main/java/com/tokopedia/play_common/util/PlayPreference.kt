package com.tokopedia.play_common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import javax.inject.Inject

/**
 * Created by jegul on 17/01/20
 */
class PlayPreference @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PLAY_PREFERENCE = "play_preference"

        private const val A_DAY_IN_MILLIS: Long = 86406000L // add delay for 5-10 s

        private const val FORMAT_ONE_TAP_ONBOARDING = "one_tap_onboarding_%s"
        private const val FORMAT_SWIPE_ONBOARDING = "swipe_onboarding_%s"

        private const val SWIPE_ONBOARDING = "new_swipe_onboarding_%s"
        private const val SWIPE_LIVE_ROOM_VARIANT = "sc_once_everyday"
        private const val SWIPE_LIVE_ROOM_DEFAULT = "swipe_onboarding_first_%s" //with userId
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

    private val currentTime: Long
        get() = System.currentTimeMillis()

    private val variant = RemoteConfigInstance.getInstance().abTestPlatform.getString(
        RollenceKey.SWIPE_LIVE_ROOM,
        ""
    )

    private fun getLastVisit(userId: String) = sharedPref.getLong(String.format(SWIPE_ONBOARDING, userId), currentTime - A_DAY_IN_MILLIS)

    private fun getDiffDay(userId: String) : Long {
        return currentTime - getLastVisit(userId)
    }

    fun setCoachMark(isFirstChannel: Boolean = false, channelId: String, userId: String) { // first channel event\
        when (variant) {
            SWIPE_LIVE_ROOM_VARIANT -> {
                if (getDiffDay(channelId) >= A_DAY_IN_MILLIS) {
                    sharedPref.edit()
                        .putLong(String.format(SWIPE_ONBOARDING, userId), System.currentTimeMillis())
                        .apply()
                }
            }
            else -> sharedPref.edit().putBoolean(String.format(SWIPE_LIVE_ROOM_DEFAULT, channelId), isFirstChannel).apply()
        }
    }

    fun isCoachMark(channelId: String,  userId: String): Boolean {
        return if (variant == SWIPE_LIVE_ROOM_VARIANT)
            getDiffDay(userId) >= A_DAY_IN_MILLIS
        else sharedPref.getBoolean(String.format(SWIPE_LIVE_ROOM_DEFAULT, channelId), false)
    }
}
