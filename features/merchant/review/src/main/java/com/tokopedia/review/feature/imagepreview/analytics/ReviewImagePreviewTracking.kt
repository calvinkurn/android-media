package com.tokopedia.review.feature.imagepreview.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ReviewImagePreviewTracking {

    fun trackOnLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        productId: String,
        isFromGallery: Boolean
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                if (isFromGallery) ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW_FROM_IMAGE_GALLERY else ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_LIKE,
                    feedbackId,
                    (!isLiked).toString()
                ),
                if (isFromGallery) ReviewImagePreviewTrackingConstants.EVENT_CATEGORY_IMAGE_GALLERY else ReviewImagePreviewTrackingConstants.EVENT_CATEGORY,
                productId
            )
        )
    }

    fun trackOnShopReviewLikeReviewClicked(feedbackId: String, isLiked: Boolean, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_LIKE,
                    feedbackId,
                    (!isLiked).toString()
                ),
                shopId
            )
        )
    }

    fun trackOnSeeAllClicked(feedbackId: String, productId: String, isFromGallery: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                if (isFromGallery) ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL_FROM_IMAGE_GALLERY else ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL,
                    feedbackId
                ),
                if (isFromGallery) ReviewImagePreviewTrackingConstants.EVENT_CATEGORY_IMAGE_GALLERY else ReviewImagePreviewTrackingConstants.EVENT_CATEGORY,
                productId
            )
        )
    }

    fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL,
                    feedbackId
                ),
                shopId
            )
        )
    }

    fun trackSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        productId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SWIPE,
                    feedbackId,
                    getImageSwipeDirection(previousIndex, currentIndex),
                    currentIndex,
                    totalImages
                ),
                ReviewImagePreviewTrackingConstants.EVENT_CATEGORY,
                productId
            )
        )
    }

    fun trackShopReviewSwipeImage(
        feedbackId: String,
        previousIndex: Int,
        currentIndex: Int,
        totalImages: Int,
        shopId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_SWIPE,
                    feedbackId,
                    getImageSwipeDirection(previousIndex, currentIndex),
                    currentIndex,
                    totalImages
                ),
                shopId
            )
        )
    }

    fun trackImpressImage(
        imageCount: Long,
        productId: String,
        attachmentId: String,
        position: Int,
        userId: String,
        trackingQueue: TrackingQueue
    ) {
        val trackingMap = DataLayer.mapOf(
            ReviewTrackingConstant.EVENT,
            ReadReviewTrackingConstants.EVENT_PROMO_VIEW,
            ReviewTrackingConstant.EVENT_ACTION,
            ReviewImagePreviewTrackingConstants.EVENT_ACTION_IMPRESS_IMAGE,
            ReviewTrackingConstant.EVENT_LABEL,
            String.format(
                ReviewImagePreviewTrackingConstants.EVENT_LABEL_IMPRESS_IMAGE,
                imageCount
            ),
            ReviewTrackingConstant.EVENT_CATEGORY,
            ReviewImagePreviewTrackingConstants.EVENT_CATEGORY_IMAGE_GALLERY,
            ReadReviewTrackingConstants.KEY_USER_ID,
            userId,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT,
            ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE,
            ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID,
            productId,
            ReadReviewTrackingConstants.KEY_ECOMMERCE,
            DataLayer.mapOf(
                ReadReviewTrackingConstants.EVENT_PROMO_VIEW, DataLayer.mapOf(
                    ReadReviewTrackingConstants.KEY_PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            ReadReviewTrackingConstants.KEY_ID, attachmentId,
                            ReadReviewTrackingConstants.KEY_CREATIVE, "",
                            ReadReviewTrackingConstants.KEY_NAME, "",
                            ReadReviewTrackingConstants.KEY_POSITION, position.toString()
                        )
                    )
                )
            )
        )
        trackingQueue.putEETracking(trackingMap as HashMap<String, Any>?)
    }

    fun trackClickReviewerName(
        isFromGallery: Boolean,
        feedbackId: String,
        userId: String,
        statistics: String,
        productId: String,
        currentUserId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewTrackingConstant.EVENT_ACTION to ReviewImagePreviewTrackingConstants.EVENT_ACTION_CLICK_REVIEWER_NAME,
                ReviewTrackingConstant.EVENT_CATEGORY to if (isFromGallery) ReviewImagePreviewTrackingConstants.EVENT_CATEGORY_REVIEW_IMAGE_GALLERY else ReviewImagePreviewTrackingConstants.EVENT_CATEGORY_REVIEW_IMAGE_READING_PAGE,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReviewImagePreviewTrackingConstants.EVENT_LABEL_CLICK_REVIEWER_NAME,
                    feedbackId,
                    userId,
                    statistics
                ),
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId,
                ReadReviewTrackingConstants.KEY_USER_ID to currentUserId
            )
        )
    }

    private fun getTrackEventMap(
        eventAction: String,
        eventLabel: String,
        eventCategory: String,
        productId: String
    ): Map<String, String> {
        return mapOf(
            ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
            ReviewTrackingConstant.EVENT_ACTION to eventAction,
            ReviewTrackingConstant.EVENT_CATEGORY to eventCategory,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        if (previousIndex < currentIndex) {
            return ReviewImagePreviewTrackingConstants.SWIPE_DIRECTION_RIGHT
        }
        return ReviewImagePreviewTrackingConstants.SWIPE_DIRECTION_LEFT
    }

    private fun getShopReviewTrackEventMap(
        eventAction: String,
        eventLabel: String,
        shopId: String
    ): Map<String, String> {
        return mapOf(
            ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
            ReviewTrackingConstant.EVENT_ACTION to eventAction,
            ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.PHYSICAL_GOODS,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_SHOP_ID to shopId
        )
    }
}