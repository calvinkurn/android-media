package com.tokopedia.reviewcommon.feature.media.gallery.base.analytic

object ReviewMediaGalleryTrackerConstant {
    const val EVENT_PROMO_VIEW = "promoView"
    const val EVENT_ACTION_IMPRESS_IMAGE = "impression - review image"
    const val EVENT_ACTION_CLICK_SWIPE = "click - swipe review image"
    const val EVENT_ACTION_CLICK_SEE_MORE = "click - lihat semua image preview"
    const val EVENT_LABEL_CLICK_SWIPE = "feedback_id:%s;direction:%s;image_position:%d;total_image:%d;"
    const val EVENT_LABEL_IMPRESS_IMAGE = "count_attachment:%d;"
    const val EVENT_LABEL_CLICK_SEE_MORE = "product_id:%s;"
    const val SWIPE_DIRECTION_RIGHT = "right"
    const val SWIPE_DIRECTION_LEFT = "left"
}