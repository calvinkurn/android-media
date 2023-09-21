package com.tokopedia.feedplus.presentation.onboarding

import android.content.Context
import androidx.core.content.edit
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
class OnBoardingPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context,
) : OnBoardingPreferences {

    private val sharedPref = context.getSharedPreferences(FEED_ONBOARDING_PREFERENCES, Context.MODE_PRIVATE)

    override fun hasShownCreateContent(): Boolean {
        return sharedPref.getBoolean(KEY_CREATE_CONTENT, false)
    }

    override fun hasShownProfileEntryPoint(): Boolean {
        return sharedPref.getBoolean(KEY_PROFILE_ENTRY_POINT, false)
    }

    override fun hasShownSwipeOnBoarding(): Boolean {
        return sharedPref.getBoolean(KEY_SWIPE_ONBOARDING, false)
    }

    override fun setHasShownCreateContent() {
        sharedPref.edit(true) {
            putBoolean(KEY_CREATE_CONTENT, true)
        }
    }

    override fun setHasShownProfileEntryPoint() {
        sharedPref.edit(true) {
            putBoolean(KEY_PROFILE_ENTRY_POINT, true)
        }
    }

    override fun setHasShownSwipeOnBoarding() {
        sharedPref.edit(true) {
            putBoolean(KEY_SWIPE_ONBOARDING, true)
        }
    }

    companion object {
        private const val FEED_ONBOARDING_PREFERENCES = "feed_onboarding_pref"

        private const val KEY_CREATE_CONTENT = "create_content"
        private const val KEY_PROFILE_ENTRY_POINT = "profile_entry_point"
        private const val KEY_SWIPE_ONBOARDING = "swipe_onboarding"
    }
}
