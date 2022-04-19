package com.tokopedia.reviewcommon.feature.media.gallery.base.analytic

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
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class ReviewMediaGalleryTracker @Inject constructor(
    private val trackingQueue: TrackingQueue
) {

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        return if (previousIndex < currentIndex) {
            ReviewMediaGalleryTrackerConstant.SWIPE_DIRECTION_RIGHT
        } else {
            ReviewMediaGalleryTrackerConstant.SWIPE_DIRECTION_LEFT
        }
    }

    fun trackSwipeImage(
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

    fun trackShopReviewSwipeImage(
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

    fun trackImpressImage(
        imageCount: Long,
        productId: String,
        attachmentId: String,
        position: Int,
        userId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            AnalyticConstant.EVENT_CATEGORY,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE,
            String.format(ReviewMediaGalleryTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productId)
            .appendPromotionsEnhancedEcommerce("", position, attachmentId, "image")
            .queueEnhancedEcommerce(trackingQueue)
    }

    fun trackImpressVideo(
        imageCount: Long,
        productId: String,
        attachmentId: String,
        position: Int,
        userId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            AnalyticConstant.EVENT_CATEGORY,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE,
            String.format(ReviewMediaGalleryTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productId)
            .appendPromotionsEnhancedEcommerce("", position, attachmentId, "video")
            .queueEnhancedEcommerce(trackingQueue)
    }

    fun trackImpressVideoV2(
        imageCount: Long,
        feedbackId: String,
        productId: String,
        attachmentId: String,
        position: Int,
        userId: String,
        videoDurationSecond: Int
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
                    feedbackId, videoDurationSecond
                ), position, attachmentId, "video"
            )
            .queueEnhancedEcommerce(trackingQueue)
    }

    fun trackPlayVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoDurationSecond: Int
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_CLICK_PG,
            AnalyticConstant.EVENT_CATEGORY_REVIEW_GALLERY_IMAGE_DETAIL,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_PLAY_VIDEO,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_PLAY_VIDEO,
                feedbackID, attachmentID, videoDurationSecond
            )
        ).appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productID)
            .sendGeneralEvent()
    }

    fun trackStopVideo(
        feedbackID: String,
        productID: String,
        attachmentID: String,
        videoDurationSecond: Int,
        watchingDurationSecond: Int
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewMediaGalleryTrackerConstant.EVENT_NAME_CLICK_PG,
            AnalyticConstant.EVENT_CATEGORY_REVIEW_GALLERY_IMAGE_DETAIL,
            ReviewMediaGalleryTrackerConstant.EVENT_ACTION_CLICK_STOP_VIDEO,
            String.format(
                ReviewMediaGalleryTrackerConstant.EVENT_LABEL_CLICK_STOP_VIDEO,
                feedbackID, attachmentID, videoDurationSecond, watchingDurationSecond
            )
        ).appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendProductId(productID)
            .sendGeneralEvent()
    }

    fun trackClickShowSeeMore(productId: String) {
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

    fun sendQueuedTrackers() {
        trackingQueue.sendAll()
    }
}