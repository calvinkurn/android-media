package com.tokopedia.review.feature.gallery.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewGalleryTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun trackOnLikeReviewClicked(feedbackId: String, isLiked: Boolean, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_LIKE, feedbackId, (!isLiked).toString()),
                productId
        ))
    }

    fun trackOnShopReviewLikeReviewClicked(feedbackId: String, isLiked: Boolean, shopId: String) {
        tracker.sendGeneralEvent(getShopReviewTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_LIKE, feedbackId, (!isLiked).toString()),
                shopId
        ))
    }

    fun trackOnSeeAllClicked(feedbackId: String, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                productId
        ))
    }

    fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {
        tracker.sendGeneralEvent(getShopReviewTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                shopId
        ))
    }

    fun trackSwipeImage(feedbackId: String, previousIndex: Int, currentIndex: Int, totalImages: Int, productId: String) {
        tracker.sendGeneralEvent(getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SWIPE, feedbackId, getImageSwipeDirection(previousIndex, currentIndex), currentIndex, totalImages),
                productId
        ))
    }

    fun trackShopReviewSwipeImage(feedbackId: String, previousIndex: Int, currentIndex: Int, totalImages: Int, shopId: String) {
        tracker.sendGeneralEvent(getShopReviewTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SWIPE,
                String.format(ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SWIPE, feedbackId, getImageSwipeDirection(previousIndex, currentIndex), currentIndex, totalImages),
                shopId
        ))
    }

    private fun getTrackEventMap(eventAction: String, eventLabel: String, productId: String): Map<String, String> {
        return mapOf(
                ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewTrackingConstant.EVENT_ACTION to eventAction,
                ReviewTrackingConstant.EVENT_CATEGORY to ReviewGalleryTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.EVENT_LABEL to eventLabel,
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getImageSwipeDirection(previousIndex: Int, currentIndex: Int): String {
        if(previousIndex < currentIndex) {
            return ReviewGalleryTrackingConstants.SWIPE_DIRECTION_RIGHT
        }
        return ReviewGalleryTrackingConstants.SWIPE_DIRECTION_LEFT
    }

    private fun getShopReviewTrackEventMap(eventAction: String, eventLabel: String, shopId: String): Map<String, String> {
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