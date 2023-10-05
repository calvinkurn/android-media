package com.tokopedia.review.feature.media.detail.analytic

object ReviewDetailTrackerConstant {
    const val EVENT_ACTION_CLICK_LIKE_REVIEW = "click - membantu on review image"
    const val EVENT_ACTION_CLICK_LIKE_REVIEW_FROM_IMAGE_GALLERY = "click - like review button"
    const val EVENT_ACTION_CLICK_SEE_ALL = "click - selengkapnya on review image"
    const val EVENT_ACTION_CLICK_SEE_ALL_FROM_IMAGE_GALLERY = "click - selengkapnya on review"
    const val EVENT_ACTION_CLICK_REVIEWER_NAME = "click - reviewer name"
    const val EVENT_CATEGORY_IMAGE_GALLERY = "product detail page - review - review gallery - image detail"
    const val EVENT_CATEGORY_REVIEW_IMAGE_READING_PAGE = "product detail page - review - review image - reading page"
    const val EVENT_CATEGORY_REVIEW_IMAGE_GALLERY = "product detail page - review - review image - gallery"
    const val EVENT_LABEL_CLICK_LIKE = "feedback_id:%s;is_active:%s;"
    const val EVENT_LABEL_CLICK_SEE_ALL = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_REVIEWER_NAME = "feedback_id:%s;user_id:%s;statistics:%s;label:%s;"
    const val TRACKER_ID_CLICK_REVIEWER_NAME_FROM_EXPANDED_REVIEW_DETAIL = "22557"
    const val TRACKER_ID_CLICK_REVIEWER_NAME_FROM_DETAILED_REVIEW_MEDIA_GALLERY = "33901"
}
