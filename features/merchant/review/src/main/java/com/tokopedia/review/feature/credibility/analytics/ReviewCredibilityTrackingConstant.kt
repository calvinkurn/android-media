package com.tokopedia.review.feature.credibility.analytics

object ReviewCredibilityTrackingConstant {
    const val EVENT_CLICK_INBOX_REVIEW = "clickInboxReview"

    const val EVENT_ACTION_CLICK_CTA_SELF = "click - cta on personal statistic bottomsheet - self"
    const val EVENT_ACTION_CLICK_CTA_OTHER_USER =
        "click - cta on personal statistic bottomsheet - other user"

    const val EVENT_CATEGORY_INBOX = "ulasan page - menunggu diulas"
    const val EVENT_CATEGORY_READING = "product detail page - review"
    const val EVENT_CATEGORY_READING_IMAGE_PREVIEW = "product detail page - review - review image - reading page"
    const val EVENT_CATEGORY_GALLERY = "product detail page - review - review image - gallery"

    const val EVENT_LABEL_CLICK_CTA = "value:%s;user_id:%s;"
}