package com.tokopedia.review.feature.media.detail.analytic

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
        feedbackId: String,
        isLiked: Boolean,
        productId: String,
        isFromGallery: Boolean
    ) {
        /** No need to track */
    }

    override fun trackOnShopReviewLikeReviewClicked(
        feedbackId: String,
        isLiked: Boolean,
        shopId: String
    ) {
        /** No need to track */
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
            eventLabel = "$feedbackId - $reviewUserId - ${getSelfOrVisitor(isReviewOwner)} - $productId"
        ).appendBusinessUnit(ReviewDetailUserProfileTrackerConstant.BUSINESS_UNIT)
            .appendCurrentSite(AnalyticConstant.CURRENT_SITE)
            .appendUserId(loggedInUserId)
            .appendTrackerIdIfNotBlank("44109")
            .appendSessionIris(TrackApp.getInstance().gtm.irisSessionId)
            .sendGeneralEvent()
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

    private fun getSelfOrVisitor(isReviewOwner: Boolean): String {
        return if (isReviewOwner) ReviewDetailUserProfileTrackerConstant.SELF
        else ReviewDetailUserProfileTrackerConstant.VISITOR
    }
}
