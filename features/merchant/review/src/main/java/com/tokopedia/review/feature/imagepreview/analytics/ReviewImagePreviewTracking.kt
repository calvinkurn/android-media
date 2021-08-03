package com.tokopedia.review.feature.imagepreview.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewImagePreviewTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun trackOnLikeReviewClicked(feedbackId: String, isLiked: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_LIKE, feedbackId, (!isLiked).toString()),
                productId
        ))
    }

    fun trackOnSeeAllClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                productId
        ))
    }

    fun trackSwipeImage(feedbackId: String, previousIndex: Int, currentIndex: Int, totalImages: Int, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SWIPE, feedbackId, getImageSwipeDirection(previousIndex, currentIndex), currentIndex, totalImages),
                productId
        ))
    }

    private fun getTrackEventMap(eventAction: String, eventLabel: String, productId: String): Map<String, String> {
        return mapOf(
                ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewTrackingConstant.EVENT_ACTION to eventAction,
                ReviewTrackingConstant.EVENT_CATEGORY to ReviewImagePreviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.EVENT_LABEL to eventLabel,
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        if(previousIndex < currentIndex) {
            return ReviewImagePreviewTrackingConstants.SWIPE_DIRECTION_RIGHT
        }
        return ReviewImagePreviewTrackingConstants.SWIPE_DIRECTION_LEFT
    }
}