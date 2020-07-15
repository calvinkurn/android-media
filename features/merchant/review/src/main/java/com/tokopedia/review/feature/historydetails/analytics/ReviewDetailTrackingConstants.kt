package com.tokopedia.review.feature.historydetails.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant

object ReviewDetailTrackingConstants {
    const val KEY_FEEDBACK_ID = "feedbackId"
    const val DETAIL_EVENT_CATEGORY = "${ReviewTrackingConstant.REVIEW_PAGE} - riwayat ulasan detail"
    const val PRODUCT_ID_EVENT_LABEL = "productId:%s;"
    const val DETAIL_SCREEN_NAME = "/riwayat-ulasan detail"
    const val CLICK_BACK_BUTTON_ACTION = "${ReviewTrackingConstant.ACTION_CLICK} - back button"
    const val CLICK_SHARE_BUTTON_ACTION = "${ReviewTrackingConstant.ACTION_CLICK} - share review"
    const val CLICK_EDIT_BUTTON_ACTION = "${ReviewTrackingConstant.ACTION_CLICK} - edit review"
    const val CLICK_PRODUCT_CARD_ACTION = "${ReviewTrackingConstant.ACTION_CLICK} - product card"
}