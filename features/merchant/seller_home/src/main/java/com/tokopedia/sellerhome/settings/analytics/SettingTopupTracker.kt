package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.track.TrackApp

const val KEY_TRACKER_ID = "trackerId"

object SettingTopupTracker {

    object TrackerId {
        const val CLICK_ATUR_AUTO_TOPUP = "31869"
    }

    private const val CLICK_TOP_ADS = "clickTopAds"
    private const val DEFAULT_BUSINESS_UNIT = "ads solution"
    private const val DEFAULT_CURRENT_SITE = "tokopediamarketplace"
    private const val CLICK_ATUR_AUTO_TOP_UP = "click - atur auto top up"
    private const val SELLER_DASHBOARD = "seller dashboard"

    fun clickAturAutoTopUp() {
        sendAnalytics(
            eventAction = CLICK_ATUR_AUTO_TOP_UP,
            trackerId = TrackerId.CLICK_ATUR_AUTO_TOPUP,
            eventCategory = SELLER_DASHBOARD,
        )
    }

    private fun sendAnalytics(
        event: String = CLICK_TOP_ADS,
        eventAction: String,
        eventCategory: String,
        eventLabel: String = "",
        trackerId: String,
        businessUnit: String = DEFAULT_BUSINESS_UNIT,
        currentSite: String = DEFAULT_CURRENT_SITE,
    ) {
        val analyticsBundle = mapOf(
            TrackingConstant.EVENT to event,
            TrackingConstant.EVENT_ACTION to eventAction,
            TrackingConstant.EVENT_CATEGORY to eventCategory,
            TrackingConstant.EVENT_LABEL to eventLabel,
            KEY_TRACKER_ID to trackerId,
            TrackingConstant.BUSINESS_UNIT to businessUnit,
            TrackingConstant.CURRENT_SITE to currentSite,
        )
        TrackApp.getInstance().gtm.apply {
            sendGeneralEvent(analyticsBundle)
        }
    }

}