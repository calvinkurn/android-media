package com.tokopedia.promousage.util.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

abstract class PromoAnalytics {

    object EventName {
        const val SELECT_CONTENT = "select_content"
        const val VIEW_ITEM = "view_item"
    }

    object EventCategory {
        const val CART = "cart"
    }

    object EventAction {
        const val CLICK_PROMO_ENTRY_POINT = "click promo entry point"
        const val IMPRESSION_PROMO_ENTRY_POINT = "impression promo entry point"
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
        const val CLICK_PROMO_ENTRY_POINT = "46340"
        const val IMPRESSION_PROMO_ENTRY_POINT = "46339"
    }

    object CustomDimension {
        const val BUSINESS_UNIT_PHYSICAL_GOODS = "physical goods"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val DIMENSION_45 = "dimension45"
    }

    protected fun sendGeneralEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String = ""
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
        )
    }

    protected fun sendEnhancedEcommerceEvent(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String = "",
        additionalData: Bundle = Bundle()
    ) {
        val data = bundleOf(
            ExtraKey.EVENT to eventName,
            ExtraKey.EVENT_NAME to eventName,
            ExtraKey.EVENT_CATEGORY to eventCategory,
            ExtraKey.EVENT_ACTION to eventAction,
            ExtraKey.EVENT_LABEL to eventLabel,
            ExtraKey.SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
        )
        data.putAll(additionalData)
        sendEnhancedEcommerceEvent(
            eventName = eventName,
            bundle = data
        )
    }

    protected fun sendEnhancedEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }
}
