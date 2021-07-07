package com.tokopedia.review.feature.reading.analytics

object ReadReviewTrackingConstants {
    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_PRODUCT_ID = "productId"

    const val BUSINESS_UNIT = "product detail page"
    const val CURRENT_SITE = "tokopediamarketplace"
    const val SCREEN_NAME = "review-pdp"

    const val EVENT_CLICK_PDP = "clickPDP"

    const val EVENT_ACTION_CLICK_POSITIVE_REVIEW_PERCENTAGE = "click - positive review percentage"
    const val EVENT_ACTION_CLICK_SEE_ALL = "click - selengkapnya on review"
    const val EVENT_ACTION_CLICK_LIKE_REVIEW = "click - membantu on review"
    const val EVENT_ACTION_CLICK_SEE_REPLY = "click - lihat balasan on review"
    const val EVENT_ACTION_CLICK_IMAGE = "click - review image on review"
    const val EVENT_ACTION_CLICK_REPORT_REVIEW = "click - laporkan ulasan"
    const val EVENT_ACTION_CLICK_CLEAR_FILTER = "click - clear filter on review"

    const val EVENT_LABEL_CLICK_POSITIVE_REVIEW_PERCENTAGE = "positive_review:%d;rating:%d;ulasan:%d;"
    const val EVENT_LABEL_CLICK_SEE_ALL = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_LIKE_REVIEW = "feedback_id:%s;is_active:%s;"
    const val EVENT_LABEL_CLICK_SEE_REPLY = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_IMAGE = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_REPORT_REVIEW = "feedback_id:%s;"

    const val EVENT_CATEGORY = "product detail page - review"
}