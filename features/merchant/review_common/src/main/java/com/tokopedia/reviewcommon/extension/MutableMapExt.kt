package com.tokopedia.reviewcommon.extension

import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.feature.media.detail.analytic.ReviewDetailTrackerConstant
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
    put(ReviewDetailTrackerConstant.KEY_ENHANCED_ECOMMERCE_USER_ID, userId)
    return this
}

fun MutableMap<String, Any>.appendProductId(productId: String): MutableMap<String, Any> {
    put(ReviewDetailTrackerConstant.KEY_PRODUCT_ID, productId)
    return this
}

fun MutableMap<String, Any>.appendShopId(shopId: String): MutableMap<String, Any> {
    put(ReviewDetailTrackerConstant.KEY_SHOP_ID, shopId)
    return this
}

fun MutableMap<String, Any>.appendBusinessUnit(businessUnit: String): MutableMap<String, Any> {
    put(ReviewDetailTrackerConstant.KEY_BUSINESS_UNIT, businessUnit)
    return this
}

fun MutableMap<String, Any>.appendCurrentSite(currentSite: String): MutableMap<String, Any> {
    put(ReviewDetailTrackerConstant.KEY_CURRENT_SITE, currentSite)
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

fun MutableMap<String, Any>.appendPromotionsEnhancedEcommerceCreativeSlot(position: Int): MutableMap<String, Any> {
    put(AnalyticConstant.KEY_CREATIVE_SLOT, position.toString())
    return this
}