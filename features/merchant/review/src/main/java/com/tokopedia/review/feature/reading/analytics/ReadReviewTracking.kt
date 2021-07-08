package com.tokopedia.review.feature.reading.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReadReviewTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun trackOpenScreen(screenName: String, productId: String) {
        tracker.sendScreenAuthenticated(screenName, getOpenScreenCustomDimensMap(productId))
    }

    fun trackOnClickPositiveReviewPercentage(positiveReview: String, rating: Int, review: Int, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_POSITIVE_REVIEW_PERCENTAGE, getPercentPositiveReview(positiveReview), rating, review),
                productId
        ))
    }

    fun trackOnFilterClicked(filterName: String, filterValue: String, isActive: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_FILTER,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_FILTER, filterName, filterValue, isActive.toString()),
                productId
        ))
    }

    fun trackOnApplyFilterClicked(filterName: String, filterValue: String, isActive: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_FILTER,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_FILTER, filterName, filterValue, isActive.toString()),
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
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_LIKE_REVIEW, feedbackId, isLiked.toString()),
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