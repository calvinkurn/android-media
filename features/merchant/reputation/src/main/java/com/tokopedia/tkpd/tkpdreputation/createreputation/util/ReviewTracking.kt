package com.tokopedia.tkpd.tkpdreputation.createreputation.util

import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTrackingConstant
import com.tokopedia.track.TrackApp
import java.util.*

object ReviewTracking {

    private fun createEventMap(event: String, category: String, action: String, label: String): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ReputationTrackingConstant.EVENT] = event
        eventMap[ReputationTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReputationTrackingConstant.EVENT_ACTION] = action
        eventMap[ReputationTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }


    fun reviewOnCloseTracker(orderId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page",
                "click - back button on product review detail page",
                "$orderId - $productId"
        ))
    }
}