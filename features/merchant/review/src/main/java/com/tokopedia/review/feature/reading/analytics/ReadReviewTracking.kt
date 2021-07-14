package com.tokopedia.review.feature.reading.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ReadReviewTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun trackOpenScreen(screenName: String, productId: String) {
        tracker.sendScreenAuthenticated(screenName, getOpenScreenCustomDimensMap(productId))
    }

    fun trackOnClickPositiveReviewPercentage(positiveReview: String, rating: Long, review: Long, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_POSITIVE_REVIEW_PERCENTAGE, getPercentPositiveReview(positiveReview), rating, review),
                productId
        ))
    }

    fun trackOnItemImpressed(feedbackId: String, position: Int, userId: String, countRating: Long, countReview: Long, characterCount: Int, imageCount: Int, trackingQueue: TrackingQueue) {
        trackingQueue.putEETracking(
                hashMapOf(
                        ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_PROMO_VIEW,
                        ReviewTrackingConstant.EVENT_ACTION to ReadReviewTrackingConstants.EVENT_ACTION_IMPRESS_ITEM,
                        ReviewTrackingConstant.EVENT_LABEL to String.format(ReadReviewTrackingConstants.EVENT_LABEL_IMPRESSION, countRating, countReview),
                        ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY,
                        ReadReviewTrackingConstants.KEY_USER_ID to userId,
                        ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                        ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                        ReadReviewTrackingConstants.KEY_ECOMMERCE to mapOf(
                                ReadReviewTrackingConstants.EVENT_PROMO_VIEW to mapOf(
                                        ReadReviewTrackingConstants.KEY_PROMOTIONS to listOf(
                                                mapOf(
                                                        ReadReviewTrackingConstants.KEY_ID to feedbackId,
                                                        ReadReviewTrackingConstants.KEY_CREATIVE to ReadReviewTrackingConstants.EVENT_CATEGORY,
                                                        ReadReviewTrackingConstants.KEY_NAME to String.format(ReadReviewTrackingConstants.EE_NAME, characterCount, imageCount),
                                                        ReadReviewTrackingConstants.KEY_POSITION to position.toString()
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun trackOnFilterClicked(filterName: String, isActive: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_FILTER,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_FILTER, filterName, (!isActive).toString()),
                productId
        ))
    }

    fun trackOnApplyFilterClicked(filterName: String, filterValue: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_FILTER,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_FILTER, filterName, filterValue),
                productId
        ))
    }

    fun trackOnApplySortClicked(sortValue: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_SORT,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_SORT, sortValue),
                productId
        ))
    }

    fun trackOnSeeFullReviewClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                productId
        ))
    }

    fun trackOnLikeClicked(feedbackId: String, isLiked: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_LIKE_REVIEW, feedbackId, (!isLiked).toString()),
                productId
        ))
    }

    fun trackOnSeeReplyClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_REPLY,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_REPLY, feedbackId),
                productId
        ))
    }

    fun trackOnImageClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_IMAGE,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_IMAGE, feedbackId),
                productId
        ))
    }

    fun trackOnReportClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_REPORT_REVIEW,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_REPORT_REVIEW, feedbackId),
                productId
        ))
    }

    fun trackOnClearFilter(productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_CLEAR_FILTER,
                "",
                productId
        ))
    }

    private fun getPercentPositiveReview(percentPositiveFormatted: String): String {
        return percentPositiveFormatted.substringBefore("%")
    }

    private fun getOpenScreenCustomDimensMap(productId: String): Map<String, String> {
        return mapOf(
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getTrackEventMap(event: String, eventAction: String, eventLabel: String, productId: String): Map<String, String> {
        return mapOf(
                ReviewTrackingConstant.EVENT to event,
                ReviewTrackingConstant.EVENT_ACTION to eventAction,
                ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.EVENT_LABEL to eventLabel,
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }
}