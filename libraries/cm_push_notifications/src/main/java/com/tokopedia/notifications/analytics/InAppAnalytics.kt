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

    private const val CATEGORY = "pop up banner"
    private const val ACTION_IMPRESSION = "view on pop up banner"
    private const val ACTION_CLICK = "click on pop up banner"
    private const val POSITION = "1"

    fun impression(data: CMInApp) {
        val promotion = mapOf(
                KEY_ID to data.getId().toString(),
                KEY_NAME to "$CATEGORY - ${data.screen}",
                KEY_CREATIVE to "",
                KEY_CREATIVE_URL to "",
                KEY_POSITION to POSITION,
                KEY_CATEGORY to "",
                KEY_PROMO_ID to "",
                KEY_PROMO_CODE to ""
        )

        sendTracker(mapOf(
                KEY_EVENT to KEY_PROMO_VIEW,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_IMPRESSION,
                KEY_EVENT_LABEL to "${data.getCampaignId()} - ${data.getCampaignUserToken()}",
                KEY_ECOMMERCE to mapOf(
                        KEY_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to listOf(promotion)
                        )
                )
        ))
    }

    fun click(data: CMInApp) {
        val promotion = mapOf(
                KEY_ID to data.getId().toString(),
                KEY_NAME to "$CATEGORY - ${data.screen}",
                KEY_CREATIVE to "",
                KEY_CREATIVE_URL to "",
                KEY_POSITION to POSITION,
                KEY_CATEGORY to "",
                KEY_PROMO_ID to "",
                KEY_PROMO_CODE to ""
        )

        val campaignCode = data.campaignCode ?: ""

        sendTracker(mapOf(
                KEY_EVENT to KEY_PROMO_CLICK,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_CLICK,
                KEY_EVENT_LABEL to "${data.getCampaignId()} - ${data.getCampaignUserToken()} ${if (campaignCode.isNotEmpty())
                    " - $campaignCode" else "" }",
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