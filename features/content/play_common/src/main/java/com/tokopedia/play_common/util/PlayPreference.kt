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

        private const val A_DAY_IN_MILLIS: Long = 86400000
        private const val BUFFER_TIME: Long = 10000

        private const val FORMAT_SWIPE_ONBOARDING = "swipe_onboarding_%s"

        private const val SWIPE_ONBOARDING = "new_swipe_onboarding_%s" //with userId
        private const val SWIPE_LIVE_ROOM_VARIANT = "sc_once_everyday"
        private const val SWIPE_LIVE_ROOM_DEFAULT = "swipe_onboarding_first_%s" //with userId
    }

    private val sharedPref = context.getSharedPreferences(PLAY_PREFERENCE, Context.MODE_PRIVATE)

    fun isOnboardingShown(tag: String): Boolean {
        return isSwipeOnboardingShown(tag)
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

    private fun getLastVisit(userId: String) = sharedPref.getLong(String.format(SWIPE_ONBOARDING, userId), 0)

    private fun getDiffDay(userId: String) : Long {
        return currentTime - getLastVisit(userId)
    }

    fun setCoachMark(userId: String, isFirstChannel: Boolean = false) {
        val newUserId = if(userId.isEmpty()) "0" else userId
        when (variant) {
            SWIPE_LIVE_ROOM_VARIANT -> {
                if (getDiffDay(newUserId) >= A_DAY_IN_MILLIS || !sharedPref.contains(String.format(SWIPE_ONBOARDING, newUserId))) {
                    sharedPref.edit()
                        .putLong(String.format(SWIPE_ONBOARDING, newUserId), currentTime)
                        .apply()
                }
            }
            else -> sharedPref.edit().putBoolean(String.format(SWIPE_LIVE_ROOM_DEFAULT, newUserId), isFirstChannel).apply()
        }
    }

    fun isCoachMark(userId: String): Boolean {
        val newUserId = if(userId.isEmpty()) "0" else userId
        return if (variant == SWIPE_LIVE_ROOM_VARIANT) {
            val diff = getDiffDay(newUserId)
            val newDiff = if (diff <= BUFFER_TIME) A_DAY_IN_MILLIS else diff // add buffer time 10s
            newDiff >= A_DAY_IN_MILLIS
        }
        else sharedPref.getBoolean(String.format(SWIPE_LIVE_ROOM_DEFAULT, newUserId), false)
    }
}
