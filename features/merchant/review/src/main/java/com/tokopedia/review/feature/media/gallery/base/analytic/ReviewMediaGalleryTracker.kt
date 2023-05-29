package com.tokopedia.review.feature.media.gallery.base.analytic

import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendPromotionsEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.appendShopId
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.queueEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

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
        productId: String,
        attachmentId: String,
        fileName: String,
        position: Int,
        userId: String
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
