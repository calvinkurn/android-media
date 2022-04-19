package com.tokopedia.gifting.tracking

import android.content.Context
import android.os.Bundle
import com.tokopedia.gifting.domain.model.Addon
import com.tokopedia.gifting.presentation.uimodel.AddOnType
import com.tokopedia.product_service_widget.R
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
        private const val KEY_COMPONENT = "component"
        private const val KEY_LAYOUT = "layout"
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_SHOP_TYPE = "shopType"
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
        private const val VALUE_EMPTY = ""

        private const val START_INDEX = 1

        private const val TYPE_RM = 0L
        private const val TYPE_PM = 1L
        private const val TYPE_OS = 2L
        private const val TYPE_PM_PLUS = 3L
        private const val TYPE_RM_CAPTION = "regular"
        private const val TYPE_GM_CAPTION = "gold_merchant"
        private const val TYPE_OS_CAPTION = "official_store"
        private const val TYPE_NOT_FOUND_CAPTION = ""
    }

    private var gtmTracker: ContextAnalytics? = null

    internal fun initializeTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().gtm
        }
        return gtmTracker!!
    }

    private fun convertToShopType(shopTier: Long): String {
        return when (shopTier) {
            TYPE_RM -> TYPE_RM_CAPTION
            TYPE_PM -> TYPE_GM_CAPTION
            TYPE_PM_PLUS -> TYPE_GM_CAPTION
            TYPE_OS -> TYPE_OS_CAPTION
            else -> TYPE_NOT_FOUND_CAPTION
        }
    }

    internal fun ContextAnalytics.sendImpressionEvent(
        addonId: String,
        action: String,
        label: String,
        category: String,
        promotionDataList: ArrayList<Bundle>,
        userId: String,
        shopIdDisplayed: String,
        shopTier: Long
    ) {
        sendEnhanceEcommerceEvent(VALUE_EVENT_NAME,
            Bundle().apply {
                putString(KEY_EVENT, VALUE_EVENT_IMPRESSION)
                putString(KEY_ACTION, action)
                putString(KEY_CATEGORY, category)
                putString(KEY_LABEL, label)

                putString(KEY_PRODUCT_ID, addonId)
                putString(KEY_COMPONENT, VALUE_EMPTY)
                putString(KEY_LAYOUT, VALUE_EMPTY)
                putString(KEY_SHOP_ID, shopIdDisplayed)
                putString(KEY_SHOP_TYPE, convertToShopType(shopTier))

                putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT)
                putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE)
                putParcelableArrayList(KEY_PROMOTIONS, promotionDataList)
                putString(KEY_USER_ID, userId)
            }
        )
    }

    internal fun ContextAnalytics.sendClickEvent(
        addonId: String,
        action: String,
        label: String,
        category: String,
        userId: String,
        shopIdDisplayed: String,
        shopTier: Long
    ) {
        val map: Map<String, String?> = mutableMapOf(
            KEY_EVENT to VALUE_EVENT_CLICK,
            KEY_ACTION to action,
            KEY_CATEGORY to category,
            KEY_LABEL to label,
            KEY_PRODUCT_ID to addonId,
            KEY_COMPONENT to VALUE_EMPTY,
            KEY_LAYOUT to VALUE_EMPTY,
            KEY_SHOP_ID to shopIdDisplayed,
            KEY_SHOP_TYPE to convertToShopType(shopTier),
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_USER_ID to userId
        )
        sendGeneralEvent(map)
    }

    fun List<Addon>.convertToPromotionData(context: Context) = ArrayList(mapIndexed { index, addon ->
        val itemName =  when (addon.basic.type) {
            AddOnType.GREETING_CARD_TYPE.name -> context.getString(R.string.gifting_greeting_card_text)
            AddOnType.GREETING_CARD_AND_PACKAGING_TYPE.name -> context.getString(R.string.gifting_greeting_card_and_package_text)
            else -> ""
        }
        Bundle().apply {
            putString(CREATIVE_NAME, addon.inventory.stock)
            putString(CREATIVE_SLOT, (index + START_INDEX).toString())
            putString(ITEM_ID, VALUE_ITEM_ID)
            putString(ITEM_NAME, itemName)
        }
    })
}