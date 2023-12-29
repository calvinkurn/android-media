package com.tokopedia.review.feature.media.gallery.base.analytic

import com.tokopedia.review.feature.media.gallery.detailed.presentation.util.DetailedReviewMediaGalleryTrackerHelper
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendPromotionsEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.queueEnhancedEcommerce
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 23, 2023
 */
class ReviewMediaGalleryUserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue
) : ReviewMediaGalleryTracker {

    override fun trackImpressImage(
        pageSource: Int,
        imageCount: Long,
        feedbackId: String,
        productId: String,
        attachmentId: String,
        fileName: String,
        position: Int,
        userId: String,
        reviewUserId: String,
        isReviewOwner: Boolean,
    ) {
        trackImpressMediaPreview(
            loggedInUserId = userId,
            feedbackId = feedbackId,
            productId = productId,
            reviewUserId = reviewUserId,
            isReviewOwner = isReviewOwner,
        )
    }

    override fun trackImpressVideoV2(
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
    ) {
        trackImpressMediaPreview(
            loggedInUserId = userId,
            feedbackId = feedbackId,
            productId = productId,
            reviewUserId = reviewUserId,
            isReviewOwner = isReviewOwner,
        )
    }

    override fun trackSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        productId: String
    ) {
        /** No need to track */
    }

    override fun trackShopReviewSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        shopId: String
    ) {
        /** No need to track */
    }

    override fun trackPlayVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long
    ) {
        /** No need to track */
    }

    override fun trackStopVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long,
        watchingDurationSecond: Long
    ) {
        /** No need to track */
    }

    override fun trackClickShowSeeMore(productId: String) {
        /** No need to track */
    }

    override fun sendQueuedTrackers() {
        trackingQueue.sendAll()
    }

    private fun trackImpressMediaPreview(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean,
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            eventCategory = DetailedReviewMediaGalleryTrackerHelper.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = DetailedReviewMediaGalleryTrackerHelper.EVENT_ACTION_IMPRESS_REVIEW_MEDIA_FROM_USER_PROFILE,
            eventLabel = DetailedReviewMediaGalleryTrackerHelper.getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44105")
            .appendBusinessUnit(DetailedReviewMediaGalleryTrackerHelper.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendPromotionsEnhancedEcommerce(
                creativeName = "",
                creativeSlot = "",
                itemId = feedbackId,
                itemName = "/feed user profile - review media",
            ).queueEnhancedEcommerce(trackingQueue)
    }
}
