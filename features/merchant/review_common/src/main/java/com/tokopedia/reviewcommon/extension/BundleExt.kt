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

fun Bundle.appendPromotionsEnhancedEcommerce(
    creativeName: String,
    creativeSlot: Int,
    itemId: String,
    itemName: String
): Bundle {
    val promotions = arrayListOf(
        Bundle().appendPromotionsEnhancedEcommerceItemId(itemId)
            .appendPromotionsEnhancedEcommerceCreativeName(creativeName)
            .appendPromotionsEnhancedEcommerceItemName(itemName)
            .appendPromotionsEnhancedEcommerceCreativeSlot(creativeSlot)
    )
    putParcelableArrayList("promotions", promotions)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceItemId(attachmentId: String): Bundle {
    putString("item_id", attachmentId)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceCreativeName(creativeName: String): Bundle {
    putString("creative_name", creativeName)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceItemName(name: String): Bundle {
    putString("item_name", name)
    return this
}

fun Bundle.appendPromotionsEnhancedEcommerceCreativeSlot(position: Int): Bundle {
    putString("creative_slot", position.toString())
    return this
}