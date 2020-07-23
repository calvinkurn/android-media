package com.tokopedia.review.feature.historydetails.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewDetailTracking {

    fun eventClickBack(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_BACK_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    fun eventClickShare(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_SHARE_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    fun eventClickEdit(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_EDIT_BUTTON_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    fun eventClickProductCard(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_PRODUCT_CARD_ACTION,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    fun eventClickImageGallery(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_REVIEW_IMAGE_GALLERY,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    fun eventClickSmiley(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_SMILEY,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_FEEDBACK_ID_EVENT_LABEL, productId.toString(), feedbackId)
                )
        )
    }

    private fun generateTrackingMap(action: String, userId: String, label: String): Map<String,String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to ReviewTrackingConstant.EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to ReviewDetailTrackingConstants.DETAIL_EVENT_CATEGORY,
                    EVENT_ACTION to action,
                    KEY_SCREEN_NAME to ReviewDetailTrackingConstants.DETAIL_SCREEN_NAME,
                    KEY_USER_ID to "$userId,",
                    EVENT_LABEL to label
            )
        }
    }


}