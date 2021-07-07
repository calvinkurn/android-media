package com.tokopedia.review.feature.gallery.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewGalleryTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun trackOnLikeReviewClicked(feedbackId: String, isLiked: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_LIKE, feedbackId, isLiked.toString()),
                productId
        ))
    }

    fun trackOnSeeAllClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                productId
        ))
    }

    fun trackSwipeImage(feedbackId: String, direction: String, imagePosition: Int, totalImages: Int, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SWIPE, ),
                productId
        ))
    }

    private fun getTrackEventMap(event: String, eventAction: String, eventLabel: String, productId: String): Map<String, String> {
        return mapOf(
                ReviewTrackingConstant.EVENT to event,
                ReviewTrackingConstant.EVENT_ACTION to eventAction,
                ReviewTrackingConstant.EVENT_CATEGORY to ReviewGalleryTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.EVENT_LABEL to eventLabel,
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }
}