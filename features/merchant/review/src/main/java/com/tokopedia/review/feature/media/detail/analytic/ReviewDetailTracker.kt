package com.tokopedia.review.feature.media.detail.analytic

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
interface ReviewDetailTracker {

    fun trackOnLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        productId: String,
        isFromGallery: Boolean
    )

    fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    )

    fun trackOnSeeAllClicked(
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean
    )

    fun trackOnShopReviewSeeAllClicked(
        feedbackId: String,
        shopId: String
    )

    fun trackClickReviewerName(
        isFromGallery: Boolean,
        feedbackId: String,
        userId: String,
        statistics: String,
        productId: String,
        currentUserId: String,
        label: String,
        trackerId: String
    )
}
