package com.tokopedia.review.feature.credibility.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewCredibilityTracking {

    fun trackOnClickCTASelfCredibility(ctaValue: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReviewCredibilityTrackingConstant.EVENT_CLICK_INBOX_REVIEW,
                ReviewTrackingConstant.EVENT_ACTION to ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA_SELF,
                ReviewTrackingConstant.EVENT_CATEGORY to ReviewCredibilityTrackingConstant.EVENT_CATEGORY_INBOX,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_VALUE,
                    ctaValue
                ),
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId
            )
        )
    }

    fun trackOnClickCTAOtherUserCredibility(ctaValue: String, userId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReviewCredibilityTrackingConstant.EVENT_CLICK_PDP,
                ReviewTrackingConstant.EVENT_ACTION to ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA_OTHER_USER,
                ReviewTrackingConstant.EVENT_CATEGORY to ReviewCredibilityTrackingConstant.EVENT_CATEGORY_READING,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_VALUE,
                    ctaValue
                ),
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to userId,
                ReviewTrackingConstant.KEY_PRODUCT_ID to productId
            )
        )
    }
}