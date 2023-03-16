package com.tokopedia.contactus.common.analytics

import android.content.Context
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.analytics.TrackingUtils
import com.tokopedia.core.analytics.nishikino.model.EventTracking

/**
 * Created by baghira on 16/07/18.
 */
object ContactUsTracking : UnifyTracking() {
    fun sendGTMInboxTicket(
        context: Context?,
        event: String?,
        category: String?,
        action: String?,
        label: String?
    ) {
        sendGTMEvent(context, EventTracking(event, category, action, label).event)
    }
}