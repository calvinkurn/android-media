package com.tokopedia.review.feature.historydetails.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp

object ReviewDetailTracking {

    fun eventClickBack(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_BACK_BUTTON_ACTION,
                        feedbackId,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_EVENT_LABEL, productId.toString())
                )
        )
    }

    fun eventClickShare(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_SHARE_BUTTON_ACTION,
                        feedbackId,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_EVENT_LABEL, productId.toString())
                )
        )
    }

    fun eventClickEdit(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_EDIT_BUTTON_ACTION,
                        feedbackId,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_EVENT_LABEL, productId.toString())
                )
        )
    }

    fun eventClickProductCard(productId: Int, feedbackId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        ReviewDetailTrackingConstants.CLICK_PRODUCT_CARD_ACTION,
                        feedbackId,
                        userId,
                        String.format(ReviewDetailTrackingConstants.PRODUCT_ID_EVENT_LABEL, productId.toString())
                )
        )
    }

    fun eventClickAttachedImage() {

    }

    private fun generateTrackingMap(action: String, feedbackId: Int, userId: String, label: String): Map<String,String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to "",
                    EVENT_CATEGORY to ReviewDetailTrackingConstants.DETAIL_EVENT_CATEGORY,
                    EVENT_ACTION to action,
                    KEY_SCREEN_NAME to ReviewDetailTrackingConstants.DETAIL_SCREEN_NAME,
                    KEY_USER_ID to "$userId,",
                    EVENT_LABEL to label,
                    ReviewDetailTrackingConstants.KEY_FEEDBACK_ID to "$feedbackId;"
            )
        }
    }


}