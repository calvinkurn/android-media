package com.tokopedia.topads.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics


private const val KEY_EVENT = "event"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val KEY_EVENT_CATEGORY_VALUE = "ads creation form"
private const val KEY_EVENT_VALUE = "clickAdsCreation"
private const val KEY_EVENT_DASHBOARD_VALUE = "clickAutoAds"
private const val KEY_EVENT_DASHBOARD_CATEGORY_VALUE = "auto ads dashboard"


class TopAdsCreateAnalytics {

    companion object {
        val topAdsCreateAnalytics: TopAdsCreateAnalytics by lazy { TopAdsCreateAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }


    fun sendTopAdsEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsDashboardEvent(eventAction: String, eventLabel: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_DASHBOARD_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_DASHBOARD_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenOnboardingScreenEvent(isLoggedInStatus: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to "openScreen",
                "screenName" to "/autoads - onboarding",
                "isLoggedInStatus" to isLoggedInStatus,
                "userId" to userId)

        getTracker().sendGeneralEvent(map)
    }
}