package com.tokopedia.review.feature.media.gallery.base.analytic

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter

interface ReviewMediaGalleryTracker {

    fun trackSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        productId: String
    )

    fun trackShopReviewSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        shopId: String
    )

    // row 22
    fun trackImpressImage(
        @ReviewMediaGalleryRouter.PageSource pageSource: Int,
        imageCount: Long,
        feedbackId: String,
        productId: String,
        attachmentId: String,
        fileName: String,
        position: Int,
        userId: String,
        reviewUserId: String,
        isReviewOwner: Boolean,
    )

    // row 33
    fun trackImpressVideoV2(
        imageCount: Long,
        feedbackId: String,
        productId: String,
        attachmentId: String,
        videoID: String,
        position: Int,
        userId: String,
        reviewUserId: String,
        isReviewOwner: Boolean,
        videoDurationSecond: Long
    )

    // row 34
    fun trackPlayVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long
    )

    // row 35
    fun trackStopVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long,
        watchingDurationSecond: Long
    )

    fun trackClickShowSeeMore(productId: String)

    fun sendQueuedTrackers()
}
