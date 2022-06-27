package com.tokopedia.topads.tracker.topup

import com.tokopedia.topads.common.analytics.*
import com.tokopedia.track.TrackApp

const val KEY_TRACKER_ID = "trackerId"

object SettingToupTracker {

    fun clickAturAutoTopUp() {
        sendAnalytics(
            eventAction = ToupTrackerEventAction.CLICK_ATUR_AUTO_TOP_UP,
            trackerId = "31869",
            eventCategory = ToupTrackerEventCategory.SELLER_DASHBOARD,
        )
    }

    private fun sendAnalytics(
        event: String = TopupTrackerEvent.CLICK_TOP_ADS,
        eventAction: String,
        eventCategory: String,
        eventLabel: String = "",
        trackerId: String,
        businessUnit: String = TopadsToupTrackerConstants.DEFAULT_BUSINESS_UNIT,
        currentSite: String = TopadsToupTrackerConstants.DEFAULT_CURRENT_SITE,
    ) {
        val analyticsBundle = mapOf(
            KEY_EVENT to event,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_CATEGORY to eventCategory,
            KEY_EVENT_LABEL to eventLabel,
            KEY_TRACKER_ID to trackerId,
            KEY_BUSINESS_UNIT_EVENT to businessUnit,
            KEY_CURRENT_SITE_EVENT to currentSite,
        )
        TrackApp.getInstance().gtm.apply {
            sendGeneralEvent(analyticsBundle)
        }
    }

}