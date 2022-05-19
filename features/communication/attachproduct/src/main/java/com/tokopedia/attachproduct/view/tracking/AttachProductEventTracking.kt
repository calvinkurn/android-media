package com.tokopedia.attachproduct.view.tracking

import android.util.Log
import java.util.*

/**
 * Created by Hendri on 08/08/18.
 */
class AttachProductEventTracking(event: String, category: String, action: String, label: String) {
    protected var eventTracking: MutableMap<String, Any> = HashMap()

    fun setEvent(event: String) {
        eventTracking["event"] = event
    }

    fun setEventCategory(eventCategory: String) {
        eventTracking["eventCategory"] = eventCategory
    }

    fun setEventAction(eventAction: String) {
        eventTracking["eventAction"] = eventAction
    }

    fun setEventLabel(eventLabel: String) {
        eventTracking["eventLabel"] = eventLabel
    }

    val event: Map<String, Any>
        get() = eventTracking

    init {
        eventTracking["event"] = event
        eventTracking["eventCategory"] = category
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
    }
}
