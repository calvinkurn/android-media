package com.tokopedia.review.feature.inbox.pending.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewPendingTracking {

    fun eventClickCard(reputationId: Int, productId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        String.format(ReviewPendingTrackingConstants.EVENT_LABEL_PENDING, reputationId.toString(), productId.toString()),
                        ReviewPendingTrackingConstants.EVENT_ACTION_CLICK_PRODUCT_CARD,
                        userId
                )
        )
    }

    fun eventClickRatingStar(reputationId: Int, productId: Int, starRating: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        String.format(ReviewPendingTrackingConstants.EVENT_LABEL_PENDING, reputationId.toString(), productId.toString()),
                        String.format(ReviewPendingTrackingConstants.EVENT_ACTION_CLICK_STAR, starRating.toString()),
                        userId
                )
        )
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun generateTrackingMap(label: String, action: String, userId: String): Map<String, String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to ReviewInboxTrackingConstants.EVENT_CATEGORY_PENDING_TAB,
                    EVENT_ACTION to action,
                    EVENT_LABEL to label,
                    KEY_SCREEN_NAME to ReviewInboxTrackingConstants.SCREEN_NAME,
                    KEY_USER_ID to "$userId,"
            )
        }
    }
}