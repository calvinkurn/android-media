package com.tokopedia.minicart.common.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.EVENT_CATEGORY_MINICART
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.EVENT_NAME_SELECT_CONTENT
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.EVENT_VIEW_ITEM
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.ITEM_ID
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.ITEM_NAME
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_BUSINESS_UNIT
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_CREATIVE_NAME
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_CREATIVE_SLOT
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_CURRENT_SITE
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_PROMOTIONS
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_TRACKER_ID
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.KEY_USER_ID
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics.Companion.VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.ACTION.EVENT_ACTION_CLICK_CARD_ON_GWP
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.ACTION.EVENT_ACTION_CLICK_SEE_ON_GWP_CARD_GIFT_LIST
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.ACTION.EVENT_ACTION_IMPRESSION_GWP_CARD
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.ACTION.EVENT_ACTION_IMPRESSION_GWP_CARD_GIFT_LIST
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.TRACKER_ID.TRACKER_ID_CLICK_CARD_ON_GWP
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.TRACKER_ID.TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_GWP_CARD
import com.tokopedia.minicart.common.analytics.MiniCartGwpAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_GWP_CARD_GIFT_LIST
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL

/**
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4429
 */
class MiniCartGwpAnalytics(
    val userId: String
) {
    private object ACTION {
        const val EVENT_ACTION_IMPRESSION_GWP_CARD_GIFT_LIST = "impression - gwp card gift list"
        const val EVENT_ACTION_CLICK_SEE_ON_GWP_CARD_GIFT_LIST = "click - lihat on gwp card gift list"
        const val EVENT_ACTION_CLICK_CARD_ON_GWP = "click - card on gwp"
        const val EVENT_ACTION_IMPRESSION_GWP_CARD = "impression - gwp card"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_GWP_CARD_GIFT_LIST = "49200"
        const val TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST = "49201"
        const val TRACKER_ID_CLICK_CARD_ON_GWP = "49203"
        const val TRACKER_ID_IMPRESSION_GWP_CARD = "49202"
    }

    private fun getDefaultDataLayer(
        eventName: String,
        eventAction: String,
        trackerId: String,
        offerId: Long,
        offerTypeId: Long,
        productIds: List<String>? = null,
        progressiveInfoText: String,
        position: Int
    ): Bundle = Bundle().apply {
        putString(EVENT, eventName)
        putString(EVENT_ACTION, eventAction)
        putString(EVENT_CATEGORY, EVENT_CATEGORY_MINICART)
        putString(EVENT_LABEL, String.EMPTY)
        putString(KEY_TRACKER_ID, trackerId)
        putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
        putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
        putString(KEY_USER_ID, userId)
        putParcelableArrayList(KEY_PROMOTIONS,
            arrayListOf(
                Bundle().apply {
                    putString(KEY_CREATIVE_NAME, progressiveInfoText)
                    putString(KEY_CREATIVE_SLOT, position.toString())
                    putString(ITEM_ID, "offer_id:$offerId;offer_type_id:$offerTypeId;")
                    putString(ITEM_NAME, "${productIds?.joinToString(",")}")
                }
            )
        )
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }

    fun sendImpressionGwpCardGiftListEvent(
        offerId: Long,
        offerTypeId: Long,
        productIds: List<String>,
        progressiveInfoText: String,
        position: Int
    ) {
        val dataLayer = getDefaultDataLayer(
            eventName = EVENT_VIEW_ITEM,
            eventAction = EVENT_ACTION_IMPRESSION_GWP_CARD_GIFT_LIST,
            trackerId = TRACKER_ID_IMPRESSION_GWP_CARD_GIFT_LIST,
            offerId = offerId,
            offerTypeId = offerTypeId,
            productIds = productIds,
            progressiveInfoText = progressiveInfoText,
            position = position
        )
        sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun sendClickSeeOnGwpCardGiftListEvent(
        offerId: Long,
        offerTypeId: Long,
        progressiveInfoText: String,
        position: Int
    ) {
        val dataLayer = getDefaultDataLayer(
            eventName = EVENT_NAME_SELECT_CONTENT,
            eventAction = EVENT_ACTION_CLICK_SEE_ON_GWP_CARD_GIFT_LIST,
            trackerId = TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST,
            offerId = offerId,
            offerTypeId = offerTypeId,
            progressiveInfoText = progressiveInfoText,
            position = position
        )
        sendEnhanceEcommerceEvent(EVENT_NAME_SELECT_CONTENT, dataLayer)
    }

    fun sendClickCardOnGwpEvent(
        offerId: Long,
        offerTypeId: Long,
        progressiveInfoText: String,
        position: Int
    ) {
        val dataLayer = getDefaultDataLayer(
            eventName = EVENT_NAME_SELECT_CONTENT,
            eventAction = EVENT_ACTION_CLICK_CARD_ON_GWP,
            trackerId = TRACKER_ID_CLICK_CARD_ON_GWP,
            offerId = offerId,
            offerTypeId = offerTypeId,
            progressiveInfoText = progressiveInfoText,
            position = position
        )
        sendEnhanceEcommerceEvent(EVENT_NAME_SELECT_CONTENT, dataLayer)
    }

    fun sendImpressionGwpCardEvent(
        offerId: Long,
        offerTypeId: Long,
        progressiveInfoText: String,
        position: Int
    ) {
        val dataLayer = getDefaultDataLayer(
            eventName = EVENT_VIEW_ITEM,
            eventAction = EVENT_ACTION_IMPRESSION_GWP_CARD,
            trackerId = TRACKER_ID_IMPRESSION_GWP_CARD,
            offerId = offerId,
            offerTypeId = offerTypeId,
            progressiveInfoText = progressiveInfoText,
            position = position
        )
        sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }
}
