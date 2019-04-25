package com.tokopedia.profile.view.preference

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by milhamj on 26/03/19.
 */
class ProfilePreference @Inject constructor(@ApplicationContext val context: Context,
                                            val userSession: UserSessionInterface) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PROFILE_PREFERENCES, Context.MODE_PRIVATE)
    }
    
    fun shouldChangeUsername(): Boolean {
        val key = String.format(SHOULD_CHANGE_USERNAME, userSession.userId)
        return sharedPreferences.getBoolean(key, true)
    }

    fun setShouldChangeUsername(shouldChangeUsername: Boolean) {
        val key = String.format(SHOULD_CHANGE_USERNAME, userSession.userId)
        sharedPreferences.edit()
                .putBoolean(key, shouldChangeUsername)
                .apply()
    }

    companion object {
        private const val PROFILE_PREFERENCES = "profile_preferences"
        private const val SHOULD_CHANGE_USERNAME = "should_change_username_%s"
    }
}