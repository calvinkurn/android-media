package com.tokopedia.tokofood.feature.home.presentation.sharedpref

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.coachmark.CoachMarkPreference
import javax.inject.Inject

class TokofoodHomeSharedPref @Inject constructor(@ApplicationContext private val context: Context) {

    fun getHasSearchCoachmarkShown(): Boolean {
        return try {
            CoachMarkPreference.hasShown(context, HAS_COACHMARK_SHOWN_KEY)
        } catch (ex: Exception) {
            false
        }
    }

    fun setHasSearchCoachmarkShown(hasShown: Boolean) {
        try {
            CoachMarkPreference.setShown(context, HAS_COACHMARK_SHOWN_KEY, hasShown)
        } catch (ex: Exception) {
            // No-Op
        }
    }

    companion object {
        private const val HAS_COACHMARK_SHOWN_KEY = "has_tokofood_coachmark_shown"
    }

}
