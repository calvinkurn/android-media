package com.tokopedia.review.feature.inbox.history.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant

object ReviewHistoryTrackingConstants {

    const val HISTORY_SCREEN_NAME = "/riwayat"
    const val HISTORY_EVENT_CATEGORY = "ulasan page - riwayat"
    const val KEYWORD_EVENT_LABEL = "keyword:%s;"
    const val EVENT_LABEL_PRODUCT_FEEDBACK_ID = "productId:%s;feedbackId:%s;"
    const val CLICK_SEARCH_EMPTY = "${ReviewTrackingConstant.ACTION_CLICK} - empty search bar"
    const val CLICK_SEARCH = "${ReviewTrackingConstant.ACTION_CLICK} - search keyword"
    const val CLICK_REVIEW_IMAGE_GALLERY = "${ReviewTrackingConstant.ACTION_CLICK} - review image gallery"
    const val CLICK_REVIEW_CARD = "${ReviewTrackingConstant.ACTION_CLICK} - review card"
}