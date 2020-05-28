package com.tokopedia.product.manage.feature.list.analytics

class EventTracking(event: String, category: String, action: String, label: String) {

    constructor(event: String, category: String, action: String, label: String, shopId: String):
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