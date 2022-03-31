package com.tokopedia.reviewcommon.extension

import android.os.Bundle
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

@Suppress("UNCHECKED_CAST")
fun <T: Any> Bundle.getSavedState(key: String, defaultValue: T?): T? {
    return get(key) as? T ?: defaultValue
}

fun Bundle.sendEnhancedEcommerce(eventName: String) {
    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
}

fun Bundle.appendGeneralEventData(
    eventName: String,
    eventCategory: String,
    eventAction: String,
    eventLabel: String
): Bundle {
    putString(TrackAppUtils.EVENT, eventName)
    putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
    putString(TrackAppUtils.EVENT_ACTION, eventAction)
    putString(TrackAppUtils.EVENT_LABEL, eventLabel)
    return this
}

fun Bundle.appendUserId(userId: String): Bundle {
    putString("userId", userId)
    return this
}

fun Bundle.appendProductId(productId: String): Bundle {
    putString("productId", productId)
    return this
}

fun Bundle.appendBusinessUnit(businessUnit: String): Bundle {
    putString("businessUnit", businessUnit)
    return this
}

fun Bundle.appendCurrentSite(currentSite: String): Bundle {
    putString("currentSite", currentSite)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerce(attachmentId: String, position: Int): Bundle {
    val promotions = listOf(
        Bundle().appendPromotionsEnhancedEcommerceId(attachmentId)
            .appendPromotionsEnhancedEcommerceCreativeName("")
            .appendPromotionsEnhancedEcommerceName("")
            .appendPromotionsEnhancedEcommercePosition(position)
    )
    val payloadPromotions = Bundle().apply {
        putParcelableArrayList("promotions", ArrayList(promotions))
    }
    val payloadPromoView = Bundle().apply {
        putBundle("promoView", payloadPromotions)
    }
    putBundle("ecommerce", payloadPromoView)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceId(attachmentId: String): Bundle {
    putString("id", attachmentId)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceCreativeName(creativeName: String): Bundle {
    putString("creative", creativeName)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceName(name: String): Bundle {
    putString("name", name)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommercePosition(position: Int): Bundle {
    putString("position", position.toString())
    return this
}