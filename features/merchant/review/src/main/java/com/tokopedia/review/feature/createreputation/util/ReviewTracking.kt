package com.tokopedia.review.feature.createreputation.util

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewTracking {

    private fun createEventMap(event: String, category: String, action: String, label: String): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
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