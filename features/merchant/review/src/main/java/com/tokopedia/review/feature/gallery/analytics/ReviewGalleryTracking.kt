package com.tokopedia.review.feature.gallery.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewGalleryTracking {

    fun trackOpenScreen(productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(ReviewGalleryTrackingConstants.SCREEN_NAME, getOpenScreenCustomDimensMap(productId))
    }

    fun trackClickImage(attachmentId: String, feedbackId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_IMAGE,
                String.format(
                    ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_IMAGE,
                    feedbackId,
                    attachmentId
                ),
                productId
            )
        )
    }

    fun trackClickSatisfactionScore(
        percentPositiveReview: String,
        rating: String,
        reviewCount: String,
        productId: String
    ) {
        val satisfactionRateInt = percentPositiveReview.filter {
            it.isDigit()
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SATISFACTION_SCORE,
                String.format(
                    ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SATISFACTION_SCORE,
                    satisfactionRateInt,
                    rating,
                    reviewCount
                ),
                productId
            )
        )
    }

    fun trackClickSeeAll(
        productId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(
                    ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL,
                    productId
                ),
                productId
            )
        )
    }

    private fun getOpenScreenCustomDimensMap(productId: String): Map<String, String> {
        return mapOf(
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getTrackEventMap(
        eventAction: String,
        eventLabel: String,
        productId: String
    ): Map<String, String> {
        return mapOf(
            ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
            ReviewTrackingConstant.EVENT_ACTION to eventAction,
            ReviewTrackingConstant.EVENT_CATEGORY to ReviewGalleryTrackingConstants.EVENT_CATEGORY,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }
}