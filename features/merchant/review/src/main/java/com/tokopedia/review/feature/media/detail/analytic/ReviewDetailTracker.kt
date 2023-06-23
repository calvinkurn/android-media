package com.tokopedia.review.feature.media.detail.analytic

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
interface ReviewDetailTracker {

    fun trackOnLikeReviewClicked(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean,
        reviewUserId: String,
        isReviewOwner: Boolean,
        isLiked: Boolean,
    )

    fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    )

    fun trackOnSeeAllClicked(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean,
        reviewUserId: String,
        isReviewOwner: Boolean,
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

    fun trackImpressOnSeeMoreBottomSheet(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean,
    )
}
