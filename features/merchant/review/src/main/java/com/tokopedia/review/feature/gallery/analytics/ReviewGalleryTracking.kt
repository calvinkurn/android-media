package com.tokopedia.review.feature.gallery.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReviewGalleryTracking @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {
    private fun trackMediaImpression(
        mediaCount: Int,
        productId: String,
        attachmentId: String,
        mediaNumber: Int,
        mediaType: String,
        mediaName: String,
        videoId: String
    ) {
        val payload = hashMapOf<String, Any>(
            TrackAppUtils.EVENT to ReviewTrackingConstant.EVENT_PROMO_VIEW,
            TrackAppUtils.EVENT_ACTION to ReviewGalleryTrackingConstants.EVENT_ACTION_VIEW_THUMBNAIL,
            TrackAppUtils.EVENT_CATEGORY to ReviewGalleryTrackingConstants.EVENT_CATEGORY,
            TrackAppUtils.EVENT_LABEL to String.format(
                ReviewGalleryTrackingConstants.EVENT_LABEL_VIEW_THUMBNAIL,
                mediaCount
            ),
            ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
            ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
            ReviewTrackingConstant.KEY_PRODUCT_ID to productId,
            ReviewTrackingConstant.KEY_USER_ID to userSession.userId,
            ReviewTrackingConstant.KEY_ECOMMERCE to hashMapOf<String, Any>(
                ReviewTrackingConstant.KEY_PROMO_VIEW to hashMapOf<String, Any>(
                    ReviewTrackingConstant.KEY_PROMOTIONS to listOf(
                        hashMapOf<String, Any>(
                            ReviewTrackingConstant.KEY_CREATIVE_NAME to "",
                            ReviewTrackingConstant.KEY_CREATIVE_SLOT to mediaNumber,
                            ReviewTrackingConstant.KEY_ITEM_ID to attachmentId,
                            ReviewTrackingConstant.KEY_ITEM_NAME to String.format(
                                ReviewGalleryTrackingConstants.ITEM_NAME_TRACK_MEDIA_IMPRESSION,
                                mediaType,
                                videoId,
                                mediaName
                            )
                        )
                    )
                )
            )
        )

        trackingQueue.putEETracking(payload)
    }

    fun trackMediaClick(
        feedbackId: String,
        productId: String,
        attachmentId: String
    ) {
        val payload = mapOf(
            TrackAppUtils.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
            TrackAppUtils.EVENT_ACTION to ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_THUMBNAIL,
            TrackAppUtils.EVENT_CATEGORY to ReviewGalleryTrackingConstants.EVENT_CATEGORY,
            TrackAppUtils.EVENT_LABEL to String.format(
                ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_THUMBNAIL,
                feedbackId,
                attachmentId
            ),
            ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
            ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
            ReviewTrackingConstant.KEY_PRODUCT_ID to productId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

    fun trackOpenScreen(productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(ReviewGalleryTrackingConstants.SCREEN_NAME, getOpenScreenCustomDimensMap(productId))
    }

    fun trackClickSatisfactionScore(
        percentPositiveReview: String,
        rating: String,
        reviewCount: String,
        productId: String
    ) {
        val satisfactionRateInt = percentPositiveReview.filter {
            it.isDigit()
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SATISFACTION_SCORE,
                String.format(
                    ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SATISFACTION_SCORE,
                    satisfactionRateInt,
                    rating,
                    reviewCount
                ),
                productId
            )
        )
    }

    fun trackClickSeeAll(
        productId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReviewGalleryTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(
                    ReviewGalleryTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL,
                    productId
                ),
                productId
            )
        )
    }

    private fun getOpenScreenCustomDimensMap(productId: String): Map<String, String> {
        return mapOf(
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getTrackEventMap(
        eventAction: String,
        eventLabel: String,
        productId: String
    ): Map<String, String> {
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

    fun trackImageImpression(
        mediaCount: Int,
        productId: String,
        attachmentId: String,
        mediaNumber: Int,
        mediaName: String
    ) {
        trackMediaImpression(mediaCount, productId, attachmentId, mediaNumber, "image", mediaName, "")
    }

    fun trackVideoImpression(
        mediaCount: Int,
        productId: String,
        attachmentId: String,
        mediaNumber: Int,
        videoId: String
    ) {
        trackMediaImpression(mediaCount, productId, attachmentId, mediaNumber, "video", "", videoId)
    }

    fun sendQueuedTrackers() {
        trackingQueue.sendAll()
    }
}
