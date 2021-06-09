package com.tokopedia.review.feature.inbox.history.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewHistoryTracking {

    fun eventSearch(userId: String, keyword: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewHistoryTrackingConstants.CLICK_SEARCH,
                        userId,
                        String.format(ReviewHistoryTrackingConstants.KEYWORD_EVENT_LABEL, keyword)
                )
        )
    }

    fun eventClickSearchBar(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewHistoryTrackingConstants.CLICK_SEARCH_EMPTY,
                        userId,
                        ""
                )
        )
    }

    fun eventClickImageGallery(userId: String, productId: Long, feedbackId: Long) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewHistoryTrackingConstants.CLICK_REVIEW_IMAGE_GALLERY,
                        userId,
                        String.format(ReviewHistoryTrackingConstants.EVENT_LABEL_PRODUCT_FEEDBACK_ID, productId.toString(), feedbackId.toString())
                )
        )
    }

    fun eventClickReviewCard(userId: String, productId: Long, feedbackId: Long) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewHistoryTrackingConstants.CLICK_REVIEW_CARD,
                        userId,
                        String.format(ReviewHistoryTrackingConstants.EVENT_LABEL_PRODUCT_FEEDBACK_ID, productId.toString(), feedbackId)
                )
        )
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun generateTrackingMap(action: String, userId: String, label: String): Map<String,String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to ReviewHistoryTrackingConstants.HISTORY_EVENT_CATEGORY,
                    EVENT_ACTION to action,
                    KEY_SCREEN_NAME to ReviewHistoryTrackingConstants.HISTORY_SCREEN_NAME,
                    KEY_USER_ID to "$userId,",
                    EVENT_LABEL to label
            )
        }
    }
}