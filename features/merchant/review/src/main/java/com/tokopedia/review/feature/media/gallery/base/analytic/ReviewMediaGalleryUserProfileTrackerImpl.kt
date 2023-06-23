package com.tokopedia.review.feature.media.gallery.base.analytic

import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
class ReviewMediaGalleryUserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue
) : ReviewMediaGalleryTracker {

    override fun trackSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        productId: String
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackShopReviewSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        shopId: String
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackImpressImage(
        pageSource: Int,
        imageCount: Long,
        productId: String,
        attachmentId: String,
        fileName: String,
        position: Int,
        userId: String
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackImpressVideoV2(
        imageCount: Long,
        feedbackId: String,
        productId: String,
        attachmentId: String,
        videoID: String,
        position: Int,
        userId: String,
        videoDurationSecond: Long
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackPlayVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackStopVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long,
        watchingDurationSecond: Long
    ) {
        /** TODO: will implement in the next PR */
    }

    override fun trackClickShowSeeMore(productId: String) {
        /** TODO: will implement in the next PR */
    }

    override fun sendQueuedTrackers() {
        /** TODO: will implement in the next PR */
    }
}
