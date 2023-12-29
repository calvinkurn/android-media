package com.tokopedia.review.feature.media.detail.analytic

import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendShopId
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.sendGeneralEvent

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
class ReviewDetailTrackerImpl : ReviewDetailTracker {

    override fun trackOnLikeReviewClicked(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean,
        reviewUserId: String,
        isReviewOwner: Boolean,
        isLiked: Boolean
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            if (isFromGallery) ReviewDetailTrackerConstant.EVENT_CATEGORY_IMAGE_GALLERY else AnalyticConstant.EVENT_CATEGORY,
            if (isFromGallery) ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_LIKE_REVIEW_FROM_IMAGE_GALLERY else ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_LIKE_REVIEW,
            String.format(
                ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_LIKE,
                feedbackId,
                (!isLiked).toString()
            )
        ).appendProductId(productId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .sendGeneralEvent()
    }

    override fun trackOnShopReviewLikeReviewClicked(feedbackId: String, isLiked: Boolean, shopId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            AnalyticConstant.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_LIKE_REVIEW,
            String.format(
                ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_LIKE,
                feedbackId,
                (!isLiked).toString()
            )
        ).appendShopId(shopId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.PHYSICAL_GOODS)
            .sendGeneralEvent()
    }

    override fun trackOnSeeAllClicked(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean,
        reviewUserId: String,
        isReviewOwner: Boolean
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            if (isFromGallery) ReviewDetailTrackerConstant.EVENT_CATEGORY_IMAGE_GALLERY else AnalyticConstant.EVENT_CATEGORY,
            if (isFromGallery) ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ALL_FROM_IMAGE_GALLERY else ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ALL,
            String.format(ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_SEE_ALL, feedbackId)
        ).appendProductId(productId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .sendGeneralEvent()
    }

    override fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PDP,
            AnalyticConstant.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ALL,
            String.format(ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_SEE_ALL, feedbackId)
        ).appendShopId(shopId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.PHYSICAL_GOODS)
            .sendGeneralEvent()
    }

    override fun trackClickReviewerName(
        isFromGallery: Boolean,
        feedbackId: String,
        userId: String,
        statistics: String,
        productId: String,
        currentUserId: String,
        label: String,
        trackerId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PG,
            if (isFromGallery) {
                ReviewDetailTrackerConstant.EVENT_CATEGORY_REVIEW_IMAGE_GALLERY
            } else {
                ReviewDetailTrackerConstant.EVENT_CATEGORY_REVIEW_IMAGE_READING_PAGE
            },
            ReviewDetailTrackerConstant.EVENT_ACTION_CLICK_REVIEWER_NAME,
            String.format(
                ReviewDetailTrackerConstant.EVENT_LABEL_CLICK_REVIEWER_NAME,
                feedbackId,
                userId,
                statistics,
                label
            )
        ).appendProductId(productId)
            .appendUserId(currentUserId)
            .appendBusinessUnit(AnalyticConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendTrackerIdIfNotBlank(trackerId)
            .sendGeneralEvent()
    }

    override fun trackImpressOnSeeMoreBottomSheet(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean
    ) {
        /** No need to track */
    }
}
