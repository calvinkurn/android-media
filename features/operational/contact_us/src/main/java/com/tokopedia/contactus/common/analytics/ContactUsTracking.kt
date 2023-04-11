package com.tokopedia.contactus.common.analytics

import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.track.builder.Tracker

object ContactUsTracking : UnifyTracking() {
    fun sendGTMInboxTicket(
        event: String,
        category: String,
        action: String,
        label: String
    ) {
        Tracker.Builder()
            .setEvent(event)
            .setEventAction(action)
            .setEventCategory(category)
            .setEventLabel(label)
            .build()
            .send()
    }
}
