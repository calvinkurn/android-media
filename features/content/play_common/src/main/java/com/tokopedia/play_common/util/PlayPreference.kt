package com.tokopedia.play_common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 17/01/20
 */
class PlayPreference @Inject constructor(
    @ApplicationContext context: Context,
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val PLAY_PREFERENCE = "play_preference"

        private const val A_DAY_IN_MILLIS: Long = 86400000

        private const val FOLLOW_POP_UP = "follow_pop_up_%1s_%2s"
    }

    private val sharedPref = context.getSharedPreferences(PLAY_PREFERENCE, Context.MODE_PRIVATE)

    /**
     * check last visit
     */

    private val currentTime: Long
        get() = System.currentTimeMillis()

    private val generateUserId: String
        get() = if(userSession.userId.isEmpty()) "0" else userSession.userId

    //StreamerId = authorId/shopId
    fun setFollowPopUp(streamerId: String) {
        if(isFollowPopup(streamerId))
            sharedPref.edit().putLong(String.format(FOLLOW_POP_UP, generateUserId, streamerId), currentTime).apply()
    }

    fun isFollowPopup(streamerId: String) : Boolean {
        return if(!sharedPref.contains(String.format(FOLLOW_POP_UP, generateUserId, streamerId)))
            true
        else (currentTime - sharedPref.getLong(String.format(FOLLOW_POP_UP, generateUserId, streamerId), 0)) >= A_DAY_IN_MILLIS
    }
}
