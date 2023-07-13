package com.tokopedia.reviewcommon.extension

import android.os.Bundle
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue

fun Map<String, Any>.sendGeneralEvent() {
    TrackApp.getInstance().gtm.sendGeneralEvent(this)
}

fun Map<String, Any>.queueEnhancedEcommerce(trackingQueue: TrackingQueue) {
    trackingQueue.putEETracking(HashMap(this))
}

fun MutableMap<String, Any>.appendGeneralEventData(
    eventName: String,
    eventCategory: String,
    eventAction: String,
    eventLabel: String
): MutableMap<String, Any> {
    put(TrackAppUtils.EVENT, eventName)
    put(TrackAppUtils.EVENT_CATEGORY, eventCategory)
    put(TrackAppUtils.EVENT_ACTION, eventAction)
    put(TrackAppUtils.EVENT_LABEL, eventLabel)
    return this
}

fun MutableMap<String, Any>.appendUserId(userId: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_USER_ID, userId)
    return this
}

fun MutableMap<String, Any>.appendProductId(productId: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_PRODUCT_ID, productId)
    return this
}

fun MutableMap<String, Any>.appendShopId(shopId: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_SHOP_ID, shopId)
    return this
}

fun MutableMap<String, Any>.appendBusinessUnit(businessUnit: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_BUSINESS_UNIT, businessUnit)
    return this
}

fun MutableMap<String, Any>.appendCurrentSite(currentSite: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_CURRENT_SITE, currentSite)
    return this
}

fun MutableMap<String, Any>.appendPageSource(source: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_PAGE_SOURCE, source)
    return this
}

fun MutableMap<String, Any>.appendTrackerIdIfNotBlank(trackerId: String): MutableMap<String, Any> {
    if (trackerId.isNotBlank()) put(AnalyticConstant.KEY_TRACKER_ID, trackerId)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerce(
    creativeName: String,
    creativeSlot: Int,
    itemId: String,
    itemName: String
): MutableMap<String, Any> {
    val promotions = arrayListOf(
        mutableMapOf<String, Any>().appendPromotionsEnhancedEcommerceItemId(itemId)
            .appendPromotionsEnhancedEcommerceCreativeName(creativeName)
            .appendPromotionsEnhancedEcommerceItemName(itemName)
            .appendPromotionsEnhancedEcommerceCreativeSlot(creativeSlot.toString())
    )
    val payloadPromotions = mapOf(AnalyticConstant.KEY_PROMOTIONS to promotions)
    val payloadPromoView = mapOf(AnalyticConstant.KEY_PROMO_VIEW to payloadPromotions)
    put(AnalyticConstant.KEY_ECOMMERCE, payloadPromoView)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerce(
    creativeName: String,
    creativeSlot: String,
    itemId: String,
    itemName: String
): MutableMap<String, Any> {
    val promotions = arrayListOf(
        mutableMapOf<String, Any>().appendPromotionsEnhancedEcommerceItemId(itemId)
            .appendPromotionsEnhancedEcommerceCreativeName(creativeName)
            .appendPromotionsEnhancedEcommerceItemName(itemName)
            .appendPromotionsEnhancedEcommerceCreativeSlot(creativeSlot)
    )
    val payloadPromotions = mapOf(AnalyticConstant.KEY_PROMOTIONS to promotions)
    val payloadPromoView = mapOf(AnalyticConstant.KEY_PROMO_VIEW to payloadPromotions)
    put(AnalyticConstant.KEY_ECOMMERCE, payloadPromoView)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerceItemId(attachmentId: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_ITEM_ID, attachmentId)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerceCreativeName(creativeName: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_CREATIVE_NAME, creativeName)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerceItemName(name: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_ITEM_NAME, name)
    return this
}

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerceCreativeSlot(position: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_CREATIVE_SLOT, position)
    return this
}

fun MutableMap<String, Any>.appendSessionIris(sessionIris: String): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_SESSION_IRIS, sessionIris)
    return this
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

fun Bundle.appendBusinessUnit(businessUnit: String): Bundle {
    putString(AnalyticConstant.KEY_BUSINESS_UNIT, businessUnit)
    return this
}

fun Bundle.appendCurrentSite(currentSite: String): Bundle {
    putString(AnalyticConstant.KEY_CURRENT_SITE, currentSite)
    return this
}

fun Bundle.appendUserId(userId: String): Bundle {
    putString(AnalyticConstant.KEY_USER_ID, userId)
    return this
}

fun Bundle.appendProductID(productID: String): Bundle {
    putString(AnalyticConstant.KEY_PRODUCT_ID, productID)
    return this
}

fun Bundle.appendPageSource(source: String): Bundle {
    putString(AnalyticConstant.KEY_PAGE_SOURCE, source)
    return this
}

fun Bundle.appendTrackerIdIfNotBlank(trackerId: String): Bundle {
    if (trackerId.isNotBlank()) putString(AnalyticConstant.KEY_TRACKER_ID, trackerId)
    return this
}

fun Bundle.sendEnhancedEcommerce(eventName: String) {
    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
}
