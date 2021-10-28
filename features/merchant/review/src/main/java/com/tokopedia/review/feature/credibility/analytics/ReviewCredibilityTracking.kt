package com.tokopedia.review.feature.credibility.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.imagepreview.presentation.fragment.ReviewImagePreviewFragment
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.track.TrackApp

object ReviewCredibilityTracking {

    fun trackOnClickCTASelfCredibility(ctaValue: String, userId: String, source: String, viewerUserId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReviewCredibilityTrackingConstant.EVENT_CLICK_INBOX_REVIEW,
                ReviewTrackingConstant.EVENT_ACTION to ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA_SELF,
                ReviewTrackingConstant.EVENT_CATEGORY to getEventCategoryBasedOnSource(source),
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_CTA,
                    ctaValue,
                    userId
                ),
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to viewerUserId
            )
        )
    }

    fun trackOnClickCTAOtherUserCredibility(ctaValue: String, userId: String, productId: String, source: String, viewerUserId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReviewCredibilityTrackingConstant.EVENT_CLICK_INBOX_REVIEW,
                ReviewTrackingConstant.EVENT_ACTION to ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA_OTHER_USER,
                ReviewTrackingConstant.EVENT_CATEGORY to getEventCategoryBasedOnSource(source),
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_CTA,
                    ctaValue,
                    userId
                ),
                ReviewTrackingConstant.KEY_BUSINESS_UNIT to ReviewTrackingConstant.BUSINESS_UNIT,
                ReviewTrackingConstant.KEY_CURRENT_SITE to ReviewTrackingConstant.CURRENT_SITE,
                ReviewTrackingConstant.KEY_USER_ID to viewerUserId,
                ReviewTrackingConstant.KEY_PRODUCT_ID to productId
            )
        )
    }

    private fun getEventCategoryBasedOnSource(source: String): String {
        return when (source) {
            ReadReviewFragment.READING_SOURCE -> ReviewCredibilityTrackingConstant.EVENT_CATEGORY_READING
            ReviewImagePreviewFragment.READING_IMAGE_PREVIEW_CREDIBILITY_SOURCE ->ReviewCredibilityTrackingConstant.EVENT_CATEGORY_READING_IMAGE_PREVIEW
            ReviewImagePreviewFragment.GALLERY_SOURCE_CREDIBILITY_SOURCE ->ReviewCredibilityTrackingConstant.EVENT_CATEGORY_GALLERY
            else -> ReviewCredibilityTrackingConstant.EVENT_CATEGORY_INBOX
        }
    }
}