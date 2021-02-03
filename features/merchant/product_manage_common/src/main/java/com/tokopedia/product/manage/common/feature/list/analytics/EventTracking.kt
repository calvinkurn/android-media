package com.tokopedia.product.manage.common.feature.list.analytics

import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.BUSINESS_UNIT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.CURRENT_SITE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.USER_ID

class EventTracking(event: String, category: String, action: String, label: String) {

    constructor(event: String, category: String, action: String, label: String, shopId: String) :
            this(event, category, action, label) {
        tracking["shopId"] = shopId
    }

    private var tracking: MutableMap<String, Any> = HashMap()

    val dataTracking: Map<String, Any> get() = tracking

    init {
        tracking["event"] = event
        tracking["eventCategory"] = category
        tracking["eventAction"] = action
        tracking["eventLabel"] = label
    }
}

fun Map<String, Any>.customDimension(userId: String, businessUnit: String, currentSite: String): Map<String, Any> {
    return with(this) {
        plus(BUSINESS_UNIT to businessUnit)
        plus(CURRENT_SITE to currentSite)
        plus(USER_ID to userId)
    }
}