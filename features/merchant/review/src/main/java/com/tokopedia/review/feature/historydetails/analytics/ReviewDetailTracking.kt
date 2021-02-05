package com.tokopedia.review.feature.historydetails.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewDetailTracking {

    fun eventClickBack(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_BACK_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    fun eventClickShare(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_SHARE_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    fun eventClickEdit(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_EDIT_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    fun eventClickProductCard(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_PRODUCT_CARD_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    fun eventClickImageGallery(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_REVIEW_IMAGE_GALLERY,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    fun eventClickSmiley(productId: Long, feedbackId: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewTrackingConstant.CLICK_SMILEY,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId, feedbackId)
                )
        )
    }

    private fun generateTrackingMap(action: String, userId: String, label: String): Map<String,String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to ReviewDetailTrackingConstants.DETAIL_EVENT_CATEGORY,
                    EVENT_ACTION to action,
                    KEY_SCREEN_NAME to ReviewDetailTrackingConstants.DETAIL_SCREEN_NAME,
                    KEY_USER_ID to "$userId,",
                    EVENT_LABEL to label
            )
        }
    }


}