package com.tokopedia.review.feature.gallery.analytics

object ReviewGalleryTrackingConstants {

    const val SCREEN_NAME = "pdp-gallery"

    const val EVENT_CATEGORY = "product detail page - review - review gallery"

    const val EVENT_ACTION_CLICK_IMAGE = "click - image detail on review gallery"
    const val EVENT_ACTION_CLICK_SATISFACTION_SCORE = "click - positive review percentage"
    const val EVENT_ACTION_CLICK_SEE_ALL = "click - lihat semua on review gallery"
    const val EVENT_ACTION_VIEW_THUMBNAIL = "view - review attachment gallery page"
    const val EVENT_ACTION_CLICK_THUMBNAIL = "click - image detail on review gallery"

    const val EVENT_LABEL_CLICK_IMAGE = "feedback_id:%s;attachment_id:%s;"
    const val EVENT_LABEL_CLICK_SATISFACTION_SCORE = "positive_review:%s;rating:%s;ulasan:%s;"
    const val EVENT_LABEL_CLICK_SEE_ALL = "product_id:%s;"
    const val EVENT_LABEL_VIEW_THUMBNAIL = "count_attachment:%s;"
    const val EVENT_LABEL_CLICK_THUMBNAIL = "feedback_id:%s;attachment_id:%s;"

    const val ITEM_NAME_TRACK_MEDIA_IMPRESSION = "media_type:%s;video_id:%s;media_filename:%s;"
}