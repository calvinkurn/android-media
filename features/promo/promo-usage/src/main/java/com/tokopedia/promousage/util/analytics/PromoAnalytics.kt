package com.tokopedia.promousage.util.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

abstract class PromoAnalytics {

    object EventName {
        const val SELECT_CONTENT = "select_content"
        const val VIEW_ITEM = "view_item"
        const val VIEW_PG_IRIS = "viewPGIris"
    }

    object EventCategory {
        const val CART = "cart"
        const val CHECKOUT = "courier selection"
        const val OCC = "order summary"
        const val PROMO = "promo page"
    }

    object EventAction {
        // Promo Usage Entry Point
        const val CLICK_CART_PROMO_ENTRY_POINT = "click promo entry point"
        const val CLICK_CHECKOUT_PROMO_ENTRY_POINT = "click user saving and promo entry point"
        const val CLICK_CHECKOUT_PROMO_ENTRY_POINT_DETAIL = "click user saving detail - total subsidy"
        const val IMPRESSION_CART_PROMO_ENTRY_POINT = "impression promo entry point"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT = "impression user saving - total subsidy"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_DETAIL = "impression user saving detail - total subsidy"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_ERROR = "impression promo entry point - error"

        // Promo Usage Bottom Sheet
        const val VIEW_AVAILABLE_PROMO_LIST_NEW = "view available promo list - new"
        const val IMPRESSION_OF_PROMO_CARD_NEW = "impression of promo card - new"
        const val CLICK_PROMO_CARD = "click promo card"
        const val CLICK_PAKAI_PROMO_PROMO_CODE = "click pakai promo promocode"
        const val CLICK_EXIT_PROMO_BOTTOMSHEET = "click exit promo bottomsheet"
    }

    object ExtraKey {
        const val BUSINESS_UNIT = "businessUnit"
        const val CREATIVE_NAME = "creative_name"
        const val CREATIVE_SLOT = "creative_slot"
        const val CURRENT_SITE = "currentSite"
        const val EVENT = "event"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_LABEL = "eventLabel"
        const val EVENT_NAME = "eventName"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val PROMO_CODE = "promoCode"
        const val PROMOTIONS = "promotions"
        const val TRACKER_ID = "trackerId"
        const val USER_ID = "userId"
        const val SESSION_IRIS = "sessionIris"
    }

    object TrackerId {
        // Promo Usage Entry Point
        const val CLICK_CART_PROMO_ENTRY_POINT = "46340"
        const val CLICK_CHECKOUT_PROMO_ENTRY_POINT = "46629"
        const val CLICK_CHECKOUT_PROMO_ENTRY_POINT_DETAIL = "46631"
        const val IMPRESSION_CART_PROMO_ENTRY_POINT = "46339"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT = "46628"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_DETAIL = "46630"
        const val IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_ERROR = "46650"

        // Promo Usage Bottom Sheet
        const val VIEW_AVAILABLE_PROMO_LIST_NEW = "47112"
        const val IMPRESSION_OF_PROMO_CARD_NEW = "47113"
        const val CLICK_PROMO_CARD = "47119"
        const val CLICK_PAKAI_PROMO_PROMO_CODE = "47121"
        const val CLICK_EXIT_PROMO_BOTTOMSHEET = "47131"
    }

    object CustomDimension {
        const val BUSINESS_UNIT_PHYSICAL_GOODS = "physical goods"
        const val BUSINESS_UNIT_PURCHASE_PLATFORM = "purchase platform"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val DIMENSION_45 = "dimension45"
        const val DIMENSION_79 = "dimension79"
    }

    protected fun sendGeneralEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String = "",
        additionalData: Map<String, Any> = emptyMap()
    ) {
        val data = TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
        data.putAll(additionalData)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    protected fun sendEnhancedEcommerceEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String = "",
        additionalData: Bundle = Bundle()
    ) {
        val data = bundleOf(
            ExtraKey.EVENT to event,
            ExtraKey.EVENT_NAME to event,
            ExtraKey.EVENT_CATEGORY to eventCategory,
            ExtraKey.EVENT_ACTION to eventAction,
            ExtraKey.EVENT_LABEL to eventLabel,
            ExtraKey.SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
        )
        data.putAll(additionalData)
        sendEnhancedEcommerceEvent(
            eventName = event,
            bundle = data
        )
    }

    private fun sendEnhancedEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }
}
