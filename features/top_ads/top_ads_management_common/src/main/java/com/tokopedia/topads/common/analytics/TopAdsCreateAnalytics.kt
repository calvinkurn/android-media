package com.tokopedia.topads.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics


private const val KEY_EVENT = "event"
private const val KEY_EVENT_SCREEN_NAME = "screenName"
private const val KEY_OPEN_SCREEN_EVENT = "openScreen"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val KEY_EVENT_CATEGORY_VALUE = "ads creation form"
private const val KEY_EVENT_CATEGORY_VALUE_EDIT = "edit group form"
private const val KEY_EVENT_VALUE_EDIT = "clickEditGroup"
private const val KEY_EVENT_VALUE = "clickAdsCreation"
private const val KEY_EVENT_DASHBOARD_VALUE = "clickAutoAds"
private const val KEY_EVENT_DASHBOARD_CATEGORY_VALUE = "auto ads dashboard"
private const val KEY_TOP_ADS_SCREEN_NAME = "/topads - home"
private const val KEY_TOP_ADS_OBAORDING_SCREEN_NAME = "/autoads - onboarding"
private const val KEY_EVENT_LOGGED_IN_STATUS = "isLoggedInStatus"
private const val KEY_EVENT_USER_ID = "userId"
private const val KEY_EVENT_INSIGHT_RECOMMENDATION = "clickShopInsight"
private const val KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION = "insight center"


class TopAdsCreateAnalytics {

    companion object {
        val topAdsCreateAnalytics: TopAdsCreateAnalytics by lazy { TopAdsCreateAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }


    fun sendTopAdsEvent(eventAction: String, eventLabel: String,userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsEventEdit(eventAction: String, eventLabel: String,userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_VALUE_EDIT,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_VALUE_EDIT,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
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

    fun sendTopAdsOpenOnboardingScreenEvent(isLoggedInStatus: Boolean, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_OBAORDING_SCREEN_NAME,
                KEY_EVENT_LOGGED_IN_STATUS to isLoggedInStatus.toString(),
                KEY_EVENT_USER_ID to userId)

        getTracker().sendGeneralEvent(map)
    }

    fun sendTopAdsOpenScreenEvent() {
        val map = mapOf(
                KEY_EVENT to KEY_OPEN_SCREEN_EVENT,
                KEY_EVENT_SCREEN_NAME to KEY_TOP_ADS_SCREEN_NAME
        )

        getTracker().sendGeneralEvent(map)
    }

    fun sendInsightGtmEvent(eventAction: String, eventLabel: String, userId: String) {
        val map = mapOf(
                KEY_EVENT to KEY_EVENT_INSIGHT_RECOMMENDATION,
                KEY_EVENT_CATEGORY to KEY_EVENT_CATEGORY_INSIGHT_RECOMMENDATION,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_EVENT_USER_ID to userId
        )

        getTracker().sendGeneralEvent(map)
    }
}