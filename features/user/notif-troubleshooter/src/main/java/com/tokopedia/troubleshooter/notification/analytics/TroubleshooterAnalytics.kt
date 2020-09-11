package com.tokopedia.troubleshooter.notification.analytics

import android.os.Bundle
import com.tokopedia.track.TrackApp

object TroubleshooterAnalytics {

    private const val EVENT_CLICK_NC = "clickNotifCenter"
    private const val EVENT_VIEW_NC = "viewNotifCenterIris"
    private const val EVENT_CATEGORY = "notif center"
    private const val EVENT_ACTION_VIEW_TS_PAGE = "view on troubleshooter page"
    private const val EVENT_ACTION_CLICK_CACHE_BTN = "click on bersihkan cache on troubleshooter"

    fun trackImpression(userId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                EVENT_VIEW_NC,
                Bundle().apply {
                    putString("eventCategory", EVENT_CATEGORY)
                    putString("eventAction", EVENT_ACTION_VIEW_TS_PAGE)
                    putString("eventLabel", "")
                    putString("userId", userId)
                    putString("shopId", shopId)
                }
        )
    }

    fun trackClearCacheClicked(userId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                EVENT_CLICK_NC,
                Bundle().apply {
                    putString("eventCategory", EVENT_CATEGORY)
                    putString("eventAction", EVENT_ACTION_CLICK_CACHE_BTN)
                    putString("eventLabel", "")
                    putString("userId", userId)
                    putString("shopId", shopId)
                }
        )
    }

}