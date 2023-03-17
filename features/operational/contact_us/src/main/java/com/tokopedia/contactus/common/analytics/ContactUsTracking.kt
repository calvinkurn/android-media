package com.tokopedia.contactus.common.analytics

import com.tokopedia.track.builder.Tracker

/**
 * Created by baghira on 16/07/18.
 */
object ContactUsTracking {
    fun sendGTMInboxTicket(
        event: String?,
        category: String?,
        action: String?,
        label: String?
    ) {
        Tracker.Builder()
            .setEvent(event?:return)
            .setEventAction(action?:return)
            .setEventCategory(category?:return)
            .setEventLabel(label?:return)
            .build()
            .send()
    }
}
