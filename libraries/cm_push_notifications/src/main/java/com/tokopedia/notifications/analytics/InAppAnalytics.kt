package com.tokopedia.notifications.analytics

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.track.TrackApp

object InAppAnalytics {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_ECOMMERCE = "ecommerce"
    private const val KEY_PROMO_VIEW = "promoView"
    private const val KEY_PROMOTIONS = "promotions"

    fun impression(
            cmInApp: CMInApp
    ) {
        val promotion = mapOf(
                "id" to cmInApp.getCampaignId(),
                "name" to cmInApp.getScreen(),
                "creative" to cmInApp.getCmLayout().getTitleText(),
                "creative_url" to cmInApp.getCmLayout().getImg(),
                "position" to "1",
                "category" to "",
                "promo_id" to "",
                "promo_code" to ""
        )

        sendTracker(mapOf(
                KEY_EVENT to "promoView",
                KEY_EVENT_CATEGORY to "pop up banner",
                KEY_EVENT_ACTION to "view on pop up banner",
                KEY_EVENT_LABEL to "${cmInApp.getScreen()} - ${cmInApp.getCampaignId()}",
                KEY_ECOMMERCE to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to promotion
                        )
                )
        ))
    }

    private fun sendTracker(dataMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataMap)
    }

}