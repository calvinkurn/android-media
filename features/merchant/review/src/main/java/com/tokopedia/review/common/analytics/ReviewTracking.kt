package com.tokopedia.review.common.analytics

import com.tokopedia.track.TrackApp
import java.util.*

object ReviewTracking {

    val tracker = TrackApp.getInstance().gtm

    fun onSuccessGetIncentiveOvoTracker(message: String?, category: String) {
        if (category.isBlank()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.VIEW_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.VIEW_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickReadSkIncentiveOvoTracker(message: String?, category: String) {
        if (category.isBlank()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoTracker(message: String?, category: String) {
        if (category.isBlank()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReviewTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isBlank()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        }
    }

    fun onClickContinueIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isBlank()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReviewTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    ReviewTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReviewTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        }
    }

    private fun createEventMap(event: String, category: String, action: String, label: String): HashMap<String, Any>? {
        val eventMap = HashMap<String, Any>()
        eventMap[ReviewTrackingConstant.EVENT] = event
        eventMap[ReviewTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReviewTrackingConstant.EVENT_ACTION] = action
        eventMap[ReviewTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

}