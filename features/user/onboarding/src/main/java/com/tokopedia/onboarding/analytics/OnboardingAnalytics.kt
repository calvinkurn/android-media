package com.tokopedia.onboarding.analytics

import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by nisie on 14/05/19.
 */
class OnboardingAnalytics @Inject constructor() {

    companion object {
        val SCREEN_ONBOARDING = "Screen OnBoarding - "
    }

    fun trackMoengage() {
        val value = HashMap<String, Any>()
        value.set("partner_source", "source_apk")

        TrackApp.getInstance().moEngage.sendTrackEvent(value,
                "Partner_Referred")

    }
}