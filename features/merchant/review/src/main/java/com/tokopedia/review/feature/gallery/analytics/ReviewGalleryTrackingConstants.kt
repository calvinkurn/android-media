package com.tokopedia.review.feature.gallery.analytics

object ReviewGalleryTrackingConstants {
    const val EVENT_ACTION_CLICK_LIKE_REVIEW = "click - membantu on review image"
    const val EVENT_ACTION_CLICK_SEE_ALL = "click - selengkapnya on review image"
    const val EVENT_ACTION_CLICK_SWIPE = "click - swipe review image"

    const val EVENT_LABEL_CLICK_LIKE = "feedback_id:%s;is_active:%s;"
    const val EVENT_LABEL_CLICK_SEE_ALL = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_SWIPE = "feedback_id:%s;direction:%s;image_position:%d;total_image:%d;"

    const val EVENT_CATEGORY = "product detail page - review - review image"

    const val SWIPE_DIRECTION_RIGHT = "right"
    const val SWIPE_DIRECTION_LEFT = "left"
}