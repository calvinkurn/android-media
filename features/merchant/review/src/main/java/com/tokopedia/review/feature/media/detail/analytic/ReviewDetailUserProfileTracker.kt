package com.tokopedia.review.feature.media.detail.analytic

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
class ReviewDetailUserProfileTracker : ReviewDetailTracker {

    override fun trackOnLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        productId: String,
        isFromGallery: Boolean
    ) {

    }

    override fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    ) {

    }

    override fun trackOnSeeAllClicked(
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean
    ) {

    }

    override fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {

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

    }
}
