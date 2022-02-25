package com.tokopedia.gifting.tracking

import android.os.Bundle
import com.tokopedia.gifting.domain.model.Addon
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

abstract class BaseGiftingTracking {

    companion object {
        // Key constant
        private const val KEY_EVENT = "event"
        private const val KEY_ACTION = "eventAction"
        private const val KEY_CATEGORY = "eventCategory"
        private const val KEY_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_USER_ID = "userId"
        private const val KEY_PROMOTIONS = "promotions"
        private const val CREATIVE_NAME = "creative_name"
        private const val CREATIVE_SLOT = "creative_slot"
        private const val ITEM_ID = "item_id"
        private const val ITEM_NAME = "item_name"

        // Values constant
        private const val VALUE_EVENT_NAME = "promoView"
        private const val VALUE_EVENT_CLICK = "clickPG"
        private const val VALUE_EVENT_IMPRESSION = "view_item"
        private const val VALUE_BUSINESS_UNIT = "product detail page"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        private const val VALUE_ITEM_ID = "pdp gifting hampers"

        private const val START_INDEX = 1
    }

    private var gtmTracker: ContextAnalytics? = null

    internal fun initializeTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().gtm
        }
        return gtmTracker!!
    }

    internal fun ContextAnalytics.sendImpressionEvent(
        action: String,
        label: String,
        category: String,
        promotionDataList: ArrayList<Bundle>,
        userId: String
    ) {
        sendEnhanceEcommerceEvent(VALUE_EVENT_NAME,
            Bundle().apply {
                putString(KEY_EVENT, VALUE_EVENT_IMPRESSION)
                putString(KEY_ACTION, action)
                putString(KEY_CATEGORY, category)
                putString(KEY_LABEL, label)
                putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT)
                putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE)
                putParcelableArrayList(KEY_PROMOTIONS, promotionDataList)
                putString(KEY_USER_ID, userId)
            }
        )
    }

    internal fun ContextAnalytics.sendClickEvent(
        action: String,
        label: String,
        category: String,
        userId: String
    ) {
        val map: Map<String, String> = mutableMapOf(
            KEY_EVENT to VALUE_EVENT_CLICK,
            KEY_ACTION to action,
            KEY_CATEGORY to category,
            KEY_LABEL to label,
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_USER_ID to userId
        )
        sendGeneralEvent(map)
    }

    fun List<Addon>.convertToPromotionData() = ArrayList(mapIndexed { index, addon ->
        Bundle().apply {
            putString(CREATIVE_NAME, addon.inventory.stock)
            putString(CREATIVE_SLOT, (index + START_INDEX).toString())
            putString(ITEM_ID, VALUE_ITEM_ID)
            putString(ITEM_NAME, addon.basic.name)
        }
    })
}