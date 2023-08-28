package com.tokopedia.stories.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 28/08/23
 */
class StoriesPreference @Inject constructor(
    @ApplicationContext context: Context,
    private val userSession: UserSessionInterface
) {
    private val userId : String
        get() = userSession.userId.ifEmpty { "0" }

    private val sharedPref = context.getSharedPreferences(STORIES_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setVisit() {
        sharedPref.edit().putBoolean(String.format(STORIES_ONBOARD_PREF, userId), true).apply()
    }

    fun isVisited() : Boolean = sharedPref.contains(String.format(STORIES_ONBOARD_PREF, userId, true))

    companion object {
        private const val STORIES_PREFERENCE_NAME = "stories_pref"
        private const val STORIES_ONBOARD_PREF = "stories_onboard"
    }
}
