package com.tokopedia.troubleshooter.notification.analytics

import com.tokopedia.track.TrackApp

object TroubleshooterAnalytics {

    private const val EVENT_CLICK_NC = "clickNotifCenter"
    private const val EVENT_VIEW_NC = "viewNotifCenterIris"
    private const val EVENT_CATEGORY = "notif center"
    private const val EVENT_ACTION_VIEW_TS_PAGE = "view on troubleshooter page"
    private const val EVENT_ACTION_CLICK_CACHE_BTN = "click on bersihkan cache on troubleshooter"

    fun trackImpression(userId: String, shopId: String) {
        val data = mapOf(
                "event" to EVENT_VIEW_NC,
                "eventCategory" to EVENT_CATEGORY,
                "eventAction" to EVENT_ACTION_VIEW_TS_PAGE,
                "eventLabel" to "",
                "userId" to userId,
                "shopId" to shopId
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun trackClearCacheClicked(userId: String, shopId: String) {
        val data = mapOf(
                "event" to EVENT_CLICK_NC,
                "eventCategory" to EVENT_CATEGORY,
                "eventAction" to EVENT_ACTION_CLICK_CACHE_BTN,
                "eventLabel" to "",
                "userId" to userId,
                "shopId" to shopId
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

}