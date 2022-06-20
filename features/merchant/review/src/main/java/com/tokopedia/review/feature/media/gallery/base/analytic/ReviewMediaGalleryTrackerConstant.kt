package com.tokopedia.review.feature.media.gallery.base.analytic

object ReviewMediaGalleryTrackerConstant {
    const val EVENT_NAME_CLICK_PG = "clickPG"
    const val EVENT_NAME_PROMO_VIEW = "promoView"
    const val EVENT_ACTION_IMPRESS_IMAGE = "impression - review image"
    const val EVENT_ACTION_IMPRESS_IMAGE_FROM_REVIEW_GALLERY = "impression - image detail on review gallery"
    const val EVENT_ACTION_CLICK_SWIPE = "click - swipe review image"
    const val EVENT_ACTION_CLICK_SEE_MORE = "click - lihat semua image preview"
    const val EVENT_ACTION_VIEW_VIDEO = "view - review video attachment"
    const val EVENT_ACTION_CLICK_PLAY_VIDEO = "click - play review video attachment"
    const val EVENT_ACTION_CLICK_STOP_VIDEO = "click - stop review video attachment"
    const val EVENT_LABEL_CLICK_SWIPE = "feedback_id:%s;direction:%s;image_position:%d;total_image:%d;"
    const val EVENT_LABEL_IMPRESS_IMAGE = "count_attachment:%d;"
    const val EVENT_LABEL_CLICK_SEE_MORE = "product_id:%s;"
    const val EVENT_LABEL_CLICK_PLAY_VIDEO = "feedback_id:%s;attachment_id:%s;video_duration:%d;media_type:video;video_id:%s;media_filename:;"
    const val EVENT_LABEL_CLICK_STOP_VIDEO = "feedback_id:%s;attachment_id:%s;video_duration:%d;watching_duration:%d;media_type:video;video_id:%s;media_filename:;"
    const val EVENT_CREATIVE_NAME_VIEW_VIDEO = "feedback_id:%s;video_duration:%d;"
    const val EVENT_ITEM_NAME_VIEW_VIDEO = "media_type:video;video_id:%s;media_filename:;"
    const val EVENT_ITEM_NAME_VIEW_IMAGE = "media_type:image;video_id:;media_filename:%s;"
    const val SWIPE_DIRECTION_RIGHT = "right"
    const val SWIPE_DIRECTION_LEFT = "left"
    const val EVENT_CATEGORY_REVIEW_GALLERY = "product detail page - review - review gallery - image detail"
}