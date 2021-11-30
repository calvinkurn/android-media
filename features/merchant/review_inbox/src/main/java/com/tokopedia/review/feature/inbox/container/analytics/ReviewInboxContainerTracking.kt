package com.tokopedia.review.feature.inbox.container.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.track.TrackApp

object ReviewInboxContainerTracking {

    fun eventOnClickReviewPendingTabFromReviewPendingTab(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(generateTrackingMap(ReviewTrackingConstant.REVIEW_PAGE, ReviewInboxTrackingConstants.EVENT_ACTION_CLICK_REVIEW_PENDING_TAB, userId, ReviewInboxTrackingConstants.MY_REVIEW_SCREEN_NAME))
    }

    fun eventOnClickReviewPendingTabFromReviewHistoryTab(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(generateTrackingMap(ReviewInboxTrackingConstants.EVENT_CATEGORY_HISTORY_TAB, ReviewInboxTrackingConstants.EVENT_ACTION_CLICK_REVIEW_PENDING_TAB, userId, ReviewInboxTrackingConstants.HISTORY_SCREEN_NAME))
    }

    fun eventOnClickBackButton(userId: String) {
        with(ReviewInboxTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(generateTrackingMap(EVENT_CATEGORY_PENDING_TAB, EVENT_ACTION_CLICK_BACK_BUTTON, userId, SCREEN_NAME))
        }
    }

    fun eventOnClickReviewHistoryTabFromReviewPendingTab(userId: String) {
        with(ReviewInboxTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(generateTrackingMap(EVENT_CATEGORY_PENDING_TAB, EVENT_ACTION_CLICK_REVIEW_HISTORY_TAB, userId, SCREEN_NAME))
        }
    }

    fun eventOnClickReviewSellerTab(userId: String) {
        with(ReviewInboxTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(generateTrackingMap(EVENT_CATEGORY_PENDING_TAB, EVENT_ACTION_CLICK_REVIEW_SELLER_TAB, userId, SCREEN_NAME))
        }
    }

    private fun generateTrackingMap(category: String, action: String, userId: String, screenName: String): Map<String, String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to category,
                    EVENT_ACTION to action,
                    EVENT_LABEL to "",
                    KEY_SCREEN_NAME to screenName,
                    KEY_USER_ID to "$userId,"
            )
        }
    }
}