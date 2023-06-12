package com.tokopedia.review.feature.media.detail.analytic

import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTrackerConstant
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendPromotionsEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.appendSessionIris
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.queueEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
class ReviewDetailUserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue
): ReviewDetailTracker {

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
            eventName = AnalyticConstant.EVENT_CLICK_CONTENT,
            eventCategory = ReviewDetailUserProfileTrackerConstant.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = ReviewDetailUserProfileTrackerConstant.EVENT_ACTION_CLICK_LIKE_FROM_USER_PROFILE,
            eventLabel = getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(ReviewDetailUserProfileTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44107")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
    }

    override fun trackOnSeeAllClicked(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        isFromGallery: Boolean,
        reviewUserId: String,
        isReviewOwner: Boolean,
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = AnalyticConstant.EVENT_CLICK_CONTENT,
            eventCategory = ReviewDetailUserProfileTrackerConstant.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = ReviewDetailUserProfileTrackerConstant.EVENT_ACTION_CLICK_SEE_ALL_FROM_USER_PROFILE,
            eventLabel = getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(ReviewDetailUserProfileTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44109")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
    }

    override fun trackImpressionReviewDetailPage(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = ReviewMediaGalleryTrackerConstant.EVENT_NAME_PROMO_VIEW,
            eventCategory = ReviewDetailUserProfileTrackerConstant.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = ReviewDetailUserProfileTrackerConstant.EVENT_ACTION_IMPRESS_REVIEW_MEDIA_FROM_USER_PROFILE,
            eventLabel = getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44105")
            .appendBusinessUnit(ReviewDetailUserProfileTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendPromotionsEnhancedEcommerce(
                creativeName = "",
                creativeSlot = "",
                itemId = feedbackId,
                itemName = "/feed user profile - review media",
            ).queueEnhancedEcommerce(trackingQueue)
    }

    override fun trackImpressOnSeeMoreBottomSheet(
        loggedInUserId: String,
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = AnalyticConstant.EVENT_VIEW_CONTENT_IRIS,
            eventCategory = ReviewDetailUserProfileTrackerConstant.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = ReviewDetailUserProfileTrackerConstant.EVENT_ACTION_IMPRESS_SEE_ALL_BOTTOM_SHEET_FROM_USER_PROFILE,
            eventLabel = getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(ReviewDetailUserProfileTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44106")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
    }

    private fun getEventLabel(
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean
    ): String {
        return "$feedbackId - $reviewUserId - ${getSelfOrVisitor(isReviewOwner)} - $productId"
    }

    private fun getSelfOrVisitor(isReviewOwner: Boolean): String {
        return if (isReviewOwner) ReviewDetailUserProfileTrackerConstant.SELF
        else ReviewDetailUserProfileTrackerConstant.VISITOR
    }

    override fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    ) {
        /** No need to track */
    }

    override fun trackOnShopReviewSeeAllClicked(feedbackId: String, shopId: String) {
        /** No need to track */
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
        /** No need to track */
    }
}
