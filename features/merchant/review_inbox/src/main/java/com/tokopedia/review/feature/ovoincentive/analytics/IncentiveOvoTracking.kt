package com.tokopedia.review.feature.ovoincentive.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp
import java.util.*

object IncentiveOvoTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun eventDismissTncBottomSheet(message: String, reputationId: String, orderId: String, productId: String, userId: String) {
        tracker.sendGeneralEvent(createEventMap(
            ReviewTrackingConstant.EVENT_CLICK_REVIEW,
            IncentiveOvoTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
            String.format(IncentiveOvoTrackingConstants.EVENT_LABEL_DISMISS_TNC, message, reputationId, orderId, productId),
            productId,
            userId
        ))
    }

    fun eventClickContinueTncBottomSheet(title: String, reputationId: String, orderId: String, productId: String, userId: String) {
        tracker.sendGeneralEvent(createEventMap(
            ReviewTrackingConstant.EVENT_CLICK_REVIEW,
            IncentiveOvoTrackingConstants.EVENT_CATEGORY_REVIEW_BOTTOM_SHEET,
            ReviewTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
            String.format(IncentiveOvoTrackingConstants.EVENT_LABEL_CLICK_CONTINUE_TNC, title, reputationId, orderId, productId),
            productId,
            userId
        ))
    }

    private fun createEventMap(event: String, category: String, action: String, label: String, productId: String, userId: String): HashMap<String, Any>? {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        eventMap[ReviewTrackingConstant.KEY_USER_ID] = userId
        eventMap[IncentiveOvoTrackingConstants.KEY_BUSINESS_UNIT] = IncentiveOvoTrackingConstants.BUSINESS_UNIT
        eventMap[IncentiveOvoTrackingConstants.KEY_CURRENT_SITE] = IncentiveOvoTrackingConstants.CURRENT_SITE
        eventMap[IncentiveOvoTrackingConstants.KEY_PRODUCT_ID] = productId
        return eventMap
    }
}