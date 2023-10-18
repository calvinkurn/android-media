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

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
class ReviewMediaGalleryTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue
) : ReviewMediaGalleryTracker {

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        return if (previousIndex < currentIndex) {
            ReviewMediaGalleryTrackerConstant.SWIPE_DIRECTION_RIGHT
        } else {
            ReviewMediaGalleryTrackerConstant.SWIPE_DIRECTION_LEFT
        }
    }

    override fun trackSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        productId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            AnalyticConstant.EVENT_CATEGORY,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_SWIPE,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_SWIPE,
                feedbackId,
                getImageSwipeDirection(previousIndex, currentIndex),
                currentIndex,
                totalImages
            )
        ).appendProductId(productId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .sendGeneralEvent()
    }

    override fun trackShopReviewSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        shopId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            AnalyticConstant.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_SWIPE,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_SWIPE,
                feedbackId,
                getImageSwipeDirection(previousIndex, currentIndex),
                currentIndex,
                totalImages
            )
        ).appendShopId(shopId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.PHYSICAL_GOODS)
            .sendGeneralEvent()
    }

    // row 22
    override fun trackImpressImage(
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
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            if (pageSource == ReviewMediaGalleryRouter.PageSource.REVIEW) {
                ReviewMediaGalleryTrackerConstant.EVENT_CATEGORY_REVIEW_GALLERY
            } else {
                AnalyticConstant.EVENT_CATEGORY
            },
            if (pageSource == ReviewMediaGalleryRouter.PageSource.REVIEW) {
                ReviewMediaGalleryTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE_FROM_REVIEW_GALLERY
            } else {
                ReviewMediaGalleryTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE
            },
            String.format(ReviewMediaGalleryTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productId)
            .appendPromotionsEnhancedEcommerce(
                "",
                position,
                attachmentId,
                String.format(
                    ReviewMediaGalleryTrackerConstant.EVENT_ITEM_NAME_VIEW_IMAGE,
                    fileName
                )
            )
            .queueEnhancedEcommerce(trackingQueue)
    }

    // row 33
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
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            AnalyticConstant.EVENT_CATEGORY_REVIEW_GALLERY_IMAGE_DETAIL,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_VIEW_VIDEO,
            String.format(ReviewMediaGalleryTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productId)
            .appendPromotionsEnhancedEcommerce(
                String.format(
                    ReviewMediaGalleryTrackerConstant.EVENT_CREATIVE_NAME_VIEW_VIDEO,
                    feedbackId,
                    videoDurationSecond
                ),
                position,
                attachmentId,
                String.format(
                    ReviewMediaGalleryTrackerConstant.EVENT_ITEM_NAME_VIEW_VIDEO, videoID
                )
            ).queueEnhancedEcommerce(trackingQueue)
    }

    // row 34
    override fun trackPlayVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_CLICK_PG,
            AnalyticConstant.EVENT_CATEGORY_REVIEW_GALLERY_IMAGE_DETAIL,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_PLAY_VIDEO,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_PLAY_VIDEO,
                feedbackID, attachmentID, videoDurationSecond, videoID
            )
        ).appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productID)
            .sendGeneralEvent()
    }

    // row 35
    override fun trackStopVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoID: String,
        videoDurationSecond: Long,
        watchingDurationSecond: Long
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_CLICK_PG,
            AnalyticConstant.EVENT_CATEGORY_REVIEW_GALLERY_IMAGE_DETAIL,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_STOP_VIDEO,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_STOP_VIDEO,
                feedbackID, attachmentID, videoDurationSecond, watchingDurationSecond, videoID
            )
        ).appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productID)
            .sendGeneralEvent()
    }

    override fun trackClickShowSeeMore(productId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            AnalyticConstant.EVENT_CATEGORY,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_SEE_MORE,
            String.format(ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_SEE_MORE, productId)
        ).appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productId)
            .sendGeneralEvent()
    }

    override fun sendQueuedTrackers() {
        trackingQueue.sendAll()
    }
}
