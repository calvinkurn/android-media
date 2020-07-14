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
    private const val KEY_PROMO_CLICK = "promoClick"
    private const val KEY_PROMOTIONS = "promotions"

    private const val KEY_ID = "id"
    private const val KEY_NAME = "name"
    private const val KEY_CREATIVE = "creative"
    private const val KEY_CREATIVE_URL = "creative_url"
    private const val KEY_POSITION = "position"
    private const val KEY_CATEGORY = "category"
    private const val KEY_PROMO_ID = "promo_id"
    private const val KEY_PROMO_CODE = "promo_code"

    fun impression(data: CMInApp) {
        val promotion = mapOf(
                KEY_ID to data.getId().toString(), // name
                KEY_NAME to data.getCmLayout().appLink, // applink
                KEY_CREATIVE to "", // campaign name
                KEY_CREATIVE_URL to "", // optional
                KEY_POSITION to "1",
                KEY_CATEGORY to "",
                KEY_PROMO_ID to "",
                KEY_PROMO_CODE to ""
        )

        sendTracker(mapOf(
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to "pop up banner",
                KEY_EVENT_ACTION to "view on pop up banner",
                KEY_EVENT_LABEL to "${data.getId()} - ${data.getCampaignUserToken()}",
                KEY_ECOMMERCE to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to listOf(promotion)
                        )
                )
        ))
    }

    fun click(data: CMInApp) {
        val promotion = mapOf(
                KEY_ID to data.getId().toString(), // name
                KEY_NAME to data.getCmLayout().appLink, // applink
                KEY_CREATIVE to "", // campaign name
                KEY_CREATIVE_URL to "", // optional
                KEY_POSITION to "1",
                KEY_CATEGORY to "",
                KEY_PROMO_ID to "",
                KEY_PROMO_CODE to ""
        )

        sendTracker(mapOf(
                KEY_EVENT to KEY_PROMO_CLICK,
                KEY_EVENT_CATEGORY to "pop up banner",
                KEY_EVENT_ACTION to "click on pop up banner",
                KEY_EVENT_LABEL to "${data.getId()} - ${data.getCampaignUserToken()}",
                KEY_ECOMMERCE to mapOf(
                        KEY_PROMO_CLICK to mapOf(
                                KEY_PROMOTIONS to listOf(promotion)
                        )
                )
        ))
    }

    private fun sendTracker(dataMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataMap)
    }

}