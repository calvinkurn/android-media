package com.tokopedia.onboarding.analytics

import android.content.Context
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by nisie on 14/05/19.
 * https://docs.google.com/spreadsheets/d/1HK3M5bcl7lNeW16WgPwbKPhcWUmPCAbR_wnO8YbdP74/edit#gid=75924383
 */
class OnboardingAnalytics @Inject constructor() {

    companion object {
        val SCREEN_ONBOARDING = "Screen OnBoarding - %s"
        var EVENT_VIEW_WELCOME_PAGE = "viewWelcomePage"

        val ACTION_ONBOARDING_LOGIN_AND_REGISTER_PAGE = "login and register page"

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
        val screenName = String.format(SCREEN_ONBOARDING, position.toString())
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    //#OB1
    fun eventOnboardingSkip(context: Context?, currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_VIEW_WELCOME_PAGE,
                String.format(SCREEN_ONBOARDING, currentPosition.toString()),
                ACTION_ONBOARDING_LOGIN_AND_REGISTER_PAGE,
                "Lewati"
        )

    }

    //#OB1
    fun trackClickLogin(currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_VIEW_WELCOME_PAGE,
                String.format(SCREEN_ONBOARDING, currentPosition.toString()),
                ACTION_ONBOARDING_LOGIN_AND_REGISTER_PAGE,
                "Masuk"
        )

    }

    //#OB1
    fun trackClickRegister(currentPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_VIEW_WELCOME_PAGE,
                String.format(SCREEN_ONBOARDING, currentPosition.toString()),
                ACTION_ONBOARDING_LOGIN_AND_REGISTER_PAGE,
                "Daftar"
        )

    }
}