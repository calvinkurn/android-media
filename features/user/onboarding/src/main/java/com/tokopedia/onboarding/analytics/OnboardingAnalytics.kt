package com.tokopedia.onboarding.analytics

import android.content.Context
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by nisie on 14/05/19.
 */
class OnboardingAnalytics @Inject constructor() {

    companion object {
        val SCREEN_ONBOARDING = "Screen OnBoarding - "
        var EVENT_ONBOARDING = "onBoardingEvent"
        var CATEGORY_ONBOARDING = "onboarding"

        var ACTION_ONBOARDING_SKIP = "click - skip button"
        var ACTION_ONBOARDING_START = "click - mulai"

        var ONBOARDING_SKIP_LABEL = "skip - "
        var ONBOARDING_START_LABEL = "click mulai sekarang"
    }

    fun trackMoengage() {
        val value = HashMap<String, Any>()
        value.set("partner_source", "source_apk")

        TrackApp.getInstance().moEngage.sendTrackEvent(value,
                "Partner_Referred")

    }

    fun sendScreen(position: Int) {
        val pageNumber = position + 1
        val screenName = SCREEN_ONBOARDING + pageNumber
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventOnboardingSkip(context: Context?, lastPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_ONBOARDING_SKIP,
                ONBOARDING_SKIP_LABEL + lastPosition
        )

    }
}