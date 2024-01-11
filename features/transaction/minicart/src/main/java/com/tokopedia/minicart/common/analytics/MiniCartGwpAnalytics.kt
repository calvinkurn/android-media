package com.tokopedia.minicart.common.analytics

import android.os.Bundle
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
import com.tokopedia.track.builder.Tracker

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
        const val EVENT_ACTION_IMPRESSION_GWP_CARD = "click - card on gwp"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_GWP_CARD_GIFT_LIST = "49200"
        const val TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST = "49201"
        const val TRACKER_ID_CLICK_CARD_ON_GWP = "49203"
        const val TRACKER_ID_IMPRESSION_GWP_CARD = "49202"

    }

    fun sendImpressionGwpCardGiftListEvent(
        offerId: Long
    ) {
        val dataLayer = Bundle().apply {
            putString(EVENT, EVENT_VIEW_ITEM)
            putString(EVENT_ACTION, EVENT_ACTION_IMPRESSION_GWP_CARD_GIFT_LIST)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_MINICART)
            putString(EVENT_LABEL, offerId.toString())
            putString(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_GWP_CARD_GIFT_LIST)
            putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
            putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(KEY_PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(KEY_CREATIVE_NAME, "")
                        putString(KEY_CREATIVE_SLOT, "")
                        putString(ITEM_ID, "")
                        putString(ITEM_NAME, "")
                    }
                )
            )
            putString(KEY_USER_ID, userId)
        }
        sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun sendClickSeeOnGwpCardGiftListEvent(
        offerId: Long
    ) {
        val dataLayer = Bundle().apply {
            putString(EVENT, EVENT_VIEW_ITEM)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_SEE_ON_GWP_CARD_GIFT_LIST)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_MINICART)
            putString(EVENT_LABEL, offerId.toString())
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST)
            putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
            putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(KEY_PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(KEY_CREATIVE_NAME, "")
                        putString(KEY_CREATIVE_SLOT, "")
                        putString(ITEM_ID, "")
                        putString(ITEM_NAME, "")
                    }
                )
            )
            putString(KEY_USER_ID, userId)
        }
        sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
        //--- test ---//
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ITEM)
            .setEventAction(EVENT_ACTION_CLICK_SEE_ON_GWP_CARD_GIFT_LIST)
            .setEventCategory(EVENT_CATEGORY_MINICART)
            .setEventLabel(offerId.toString())
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_SEE_ON_GWP_CARD_GIFT_LIST)
            .setBusinessUnit(VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
            .setCurrentSite(VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCardOnGwpEvent(offerId: Long) {
        Tracker.Builder()
            .setEvent(EVENT_NAME_SELECT_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_CARD_ON_GWP)
            .setEventCategory(EVENT_CATEGORY_MINICART)
            .setEventLabel(offerId.toString())
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CARD_ON_GWP)
            .setBusinessUnit(VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
            .setCurrentSite(VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionGwpCardEvent(offerId: Long) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ITEM)
            .setEventAction(EVENT_ACTION_IMPRESSION_GWP_CARD)
            .setEventCategory(EVENT_CATEGORY_MINICART)
            .setEventLabel(offerId.toString())
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_GWP_CARD)
            .setBusinessUnit(VALUE_BUSINESS_UNIT_TOKOPEDIA_MARKETPLACE)
            .setCurrentSite(VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }
}
