package com.tokopedia.onboarding.analytics

import android.os.Build
import com.tokopedia.track.TrackApp
import timber.log.Timber

/**
 * Created by Ade Fulki on 2020-02-08.
 * ade.hadian@tokopedia.com
 */

class OnboardingAnalytics {

    fun trackScreen(position: Int, variant: String) {
        val screenName = String.format(SCREEN_ONBOARDING, position.toString(), variant)
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | 
            |${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | 
            |${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""".trimMargin())
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackMoengage() {
        val value = HashMap<String, Any>()
        value.set("partner_source", "source_apk")

        TrackApp.getInstance().moEngage.sendTrackEvent(value,
                "Partner_Referred")

    }

    fun eventOnboardingSkip(currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_CLICK_ON_BUTTON_SKIP,
                String.format(LABEL_SKIP, currentPosition.toString())
        )
    }

    fun eventOnboardingNext(currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_CLICK_ON_BUTTON_SELANJUTNYA,
                currentPosition.toString()
        )
    }

    fun eventOnboardingJoin(currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_CLICK_ONBOARDING_MAIN_BUTTON,
                String.format(LABEL_LANDING_PAGE_ONBOARDING, currentPosition.toString())
        )
    }

    companion object {
        const val SCREEN_ONBOARDING = "Screen OnBoarding - %s%s"

        private const val EVENT_CLICK_ONBOARDING = "clickOnboarding"
        private const val EVENT_ONBOARDING = "onBoardingEvent"

        private const val CATEGORY_ONBOARDING = "onboarding"

        private const val ACTION_CLICK_ONBOARDING_MAIN_BUTTON = "click onboarding main button"
        private const val ACTION_CLICK_ON_BUTTON_SELANJUTNYA = "click on button selanjutnya"
        private const val ACTION_CLICK_ON_BUTTON_SKIP = "click - skip button"

        private const val LABEL_SKIP = "skip - %s"
        private const val LABEL_LANDING_PAGE_ONBOARDING = "landing page onboarding - %s"
    }
}