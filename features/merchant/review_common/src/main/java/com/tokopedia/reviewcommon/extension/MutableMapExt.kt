package com.tokopedia.reviewcommon.extension

import com.tokopedia.reviewcommon.feature.media.detail.analytic.ReviewDetailTrackerConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

fun Map<String, Any>.sendGeneralEvent() {
    TrackApp.getInstance().gtm.sendGeneralEvent(this)
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