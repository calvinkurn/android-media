package com.tokopedia.product.manage.oldlist.analytics

import timber.log.Timber
import java.util.*

class EventTracking(event: String, category: String, action: String, label: String) {

    private var eventTracking: MutableMap<String, Any> = HashMap()

    val event: Map<String, Any> get() = eventTracking

    init {
        Timber.d("GAv4 EventTracking: $event $category $action $label")
        eventTracking["event"] = event
        eventTracking["eventCategory"] = category
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
    }
}