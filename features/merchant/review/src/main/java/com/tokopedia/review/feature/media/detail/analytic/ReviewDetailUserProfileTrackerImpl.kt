package com.tokopedia.review.feature.media.detail.analytic

import android.util.Log

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
class ReviewDetailUserProfileTrackerImpl : ReviewDetailTracker {

    override fun trackOnLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        productId: String,
        isFromGallery: Boolean
    ) {
        Log.d("<LOG>", "ReviewDetailUserProfileTracker will be provided in the next PR")
    }

    override fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    ) {
        Log.d("<LOG>", "ReviewDetailUserProfileTracker will be provided in the next PR")
    }

    override fun trackOnSeeAllClicked(
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean
    ) {
        Log.d("<LOG>", "ReviewDetailUserProfileTracker will be provided in the next PR")
    }

    override fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {
        Log.d("<LOG>", "ReviewDetailUserProfileTracker will be provided in the next PR")
    }

    override fun trackClickReviewerName(
        isFromGallery: Boolean,
        feedbackId: String,
        userId: String,
        statistics: String,
        productId: String,
        currentUserId: String,
        label: String,
        trackerId: String
    ) {
        Log.d("<LOG>", "ReviewDetailUserProfileTracker will be provided in the next PR")
    }
}
