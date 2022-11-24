package com.tokopedia.tokofood.feature.home.presentation.sharedpref

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.coachmark.CoachMarkPreference
import javax.inject.Inject

class TokofoodHomeSharedPref @Inject constructor(@ApplicationContext private val context: Context) {

    fun getHasSearchCoachmarkShown(): Boolean {
        return CoachMarkPreference.hasShown(context, HAS_COACHMARK_SHOWN_KEY)
    }

    fun setHasSearchCoachmarkShown(hasShown: Boolean) {
        CoachMarkPreference.setShown(context, HAS_COACHMARK_SHOWN_KEY, hasShown)
    }

    companion object {
        private const val HAS_COACHMARK_SHOWN_KEY = "has_tokofood_coachmark_shown"
    }

}
