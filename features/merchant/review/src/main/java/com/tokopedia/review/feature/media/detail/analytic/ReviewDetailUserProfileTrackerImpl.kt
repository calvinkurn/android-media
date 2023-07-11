package com.tokopedia.review.feature.media.detail.analytic

import com.tokopedia.review.feature.media.gallery.detailed.presentation.util.DetailedReviewMediaGalleryTrackerHelper
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendSessionIris
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
class ReviewDetailUserProfileTrackerImpl @Inject constructor(): ReviewDetailTracker {

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
            eventCategory = DetailedReviewMediaGalleryTrackerHelper.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = DetailedReviewMediaGalleryTrackerHelper.EVENT_ACTION_CLICK_LIKE_FROM_USER_PROFILE,
            eventLabel = DetailedReviewMediaGalleryTrackerHelper.getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(DetailedReviewMediaGalleryTrackerHelper.BUSINESS_UNIT)
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
            eventCategory = DetailedReviewMediaGalleryTrackerHelper.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = DetailedReviewMediaGalleryTrackerHelper.EVENT_ACTION_CLICK_SEE_ALL_FROM_USER_PROFILE,
            eventLabel = DetailedReviewMediaGalleryTrackerHelper.getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(DetailedReviewMediaGalleryTrackerHelper.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44109")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
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
            eventCategory = DetailedReviewMediaGalleryTrackerHelper.EVENT_CATEGORY_FEED_USER_PROFILE,
            eventAction = DetailedReviewMediaGalleryTrackerHelper.EVENT_ACTION_IMPRESS_SEE_ALL_BOTTOM_SHEET_FROM_USER_PROFILE,
            eventLabel = DetailedReviewMediaGalleryTrackerHelper.getEventLabel(feedbackId, productId, reviewUserId, isReviewOwner)
        ).appendBusinessUnit(DetailedReviewMediaGalleryTrackerHelper.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44106")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
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
