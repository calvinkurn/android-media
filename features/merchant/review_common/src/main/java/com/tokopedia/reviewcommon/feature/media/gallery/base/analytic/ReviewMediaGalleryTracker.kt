package com.tokopedia.reviewcommon.feature.media.gallery.base.analytic

import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendPromotionsEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.appendShopId
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.queueEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
import com.tokopedia.reviewcommon.feature.media.detail.analytic.ReviewDetailTrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class ReviewMediaGalleryTracker @Inject constructor(
    private val trackingQueue: TrackingQueue
) {

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        return if (previousIndex < currentIndex) {
            ReviewDetailTrackerConstant.SWIPE_DIRECTION_RIGHT
        } else {
            ReviewDetailTrackerConstant.SWIPE_DIRECTION_LEFT
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
            ReviewDetailTrackerConstant.EVENT_CLICK_PDP,
            ReviewDetailTrackerConstant.EVENT_CATEGORY,
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SWIPE,
            String.format(
                ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_SWIPE,
                feedbackId,
                getImageSwipeDirection(previousIndex, currentIndex),
                currentIndex,
                totalImages
            )
        ).appendProductId(productId)
            .appendBusinessUnit(ReviewDetailTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewDetailTrackerConstant.CURRENT_SITE)
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
            ReviewDetailTrackerConstant.EVENT_CLICK_PDP,
            ReviewDetailTrackerConstant.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SWIPE,
            String.format(
                ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_SWIPE,
                feedbackId,
                getImageSwipeDirection(previousIndex, currentIndex),
                currentIndex,
                totalImages
            )
        ).appendShopId(shopId)
            .appendBusinessUnit(ReviewDetailTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewDetailTrackerConstant.PHYSICAL_GOODS)
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
            ReviewDetailTrackerConstant.EVENT_VIEW_ITEM,
            ReviewDetailTrackerConstant.EVENT_CATEGORY,
            ReviewDetailTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE,
            String.format(ReviewDetailTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(ReviewDetailTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewDetailTrackerConstant.CURRENT_SITE)
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
            ReviewDetailTrackerConstant.EVENT_VIEW_ITEM,
            ReviewDetailTrackerConstant.EVENT_CATEGORY,
            ReviewDetailTrackerConstant.EVENT_ACTION_IMPRESS_IMAGE,
            String.format(ReviewDetailTrackerConstant.EVENT_LABEL_IMPRESS_IMAGE, imageCount)
        ).appendUserId(userId)
            .appendBusinessUnit(ReviewDetailTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewDetailTrackerConstant.CURRENT_SITE)
            .appendProductId(productId)
            .appendPromotionsEnhancedEcommerce("", position, attachmentId, "video")
            .queueEnhancedEcommerce(trackingQueue)
    }

    fun trackClickShowSeeMore(productId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            ReviewDetailTrackerConstant.EVENT_CLICK_PDP,
            ReviewDetailTrackerConstant.EVENT_CATEGORY,
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_MORE,
            String.format(ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_SEE_MORE, productId)
        ).appendBusinessUnit(ReviewDetailTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewDetailTrackerConstant.CURRENT_SITE)
            .appendProductId(productId)
            .sendGeneralEvent()
    }

    fun sendQueuedTrackers() {
        trackingQueue.sendAll()
    }
}