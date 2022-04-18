package com.tokopedia.reviewcommon.feature.media.detail.analytic

object ReviewDetailTrackerConstant {
    const val KEY_PRODUCT_ID = "productId"
    const val KEY_SHOP_ID = "shopId"
    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_CURRENT_SITE = "currentSite"

    const val KEY_ENHANCED_ECOMMERCE_USER_ID = "userId"

    const val EVENT_CLICK_PDP = "clickPDP"
    const val EVENT_PROMO_VIEW = "promoView"
    const val EVENT_ACTION_CLICK_LIKE_REVIEW = "click - membantu on review image"
    const val EVENT_ACTION_CLICK_LIKE_REVIEW_FROM_IMAGE_GALLERY = "click - like review button"
    const val EVENT_ACTION_CLICK_SEE_ALL = "click - selengkapnya on review image"
    const val EVENT_ACTION_CLICK_SEE_ALL_FROM_IMAGE_GALLERY = "click - selengkapnya on review"
    const val EVENT_ACTION_CLICK_SWIPE = "click - swipe review image"
    const val EVENT_ACTION_IMPRESS_IMAGE = "impression - review image"
    const val EVENT_ACTION_CLICK_REVIEWER_NAME = "click - reviewer name"
    const val EVENT_ACTION_CLICK_SEE_MORE = "click - lihat semua image preview"
    const val EVENT_CATEGORY = "product detail page - review - review image"
    const val EVENT_CATEGORY_SHOP_REVIEW = "shop page - buyer - review"
    const val EVENT_CATEGORY_IMAGE_GALLERY = "product detail page - review - review gallery - image detail"
    const val EVENT_CATEGORY_REVIEW_IMAGE_READING_PAGE = "product detail page - review - review image - reading page"
    const val EVENT_CATEGORY_REVIEW_IMAGE_GALLERY = "product detail page - review - review image - gallery"
    const val EVENT_LABEL_CLICK_LIKE = "feedback_id:%s;is_active:%s;"
    const val EVENT_LABEL_CLICK_SEE_ALL = "feedback_id:%s;"
    const val EVENT_LABEL_CLICK_SWIPE = "feedback_id:%s;direction:%s;image_position:%d;total_image:%d;"
    const val EVENT_LABEL_IMPRESS_IMAGE = "count_attachment:%d;"
    const val EVENT_LABEL_CLICK_REVIEWER_NAME = "feedback_id:%s;user_id:%s;statistics:%s;"
    const val EVENT_LABEL_CLICK_SEE_MORE = "product_id:%s;"

    const val BUSINESS_UNIT = "product detail page"
    const val PHYSICAL_GOODS = "physical goods"
    const val CURRENT_SITE = "tokopediamarketplace"
    const val SWIPE_DIRECTION_RIGHT = "right"
    const val SWIPE_DIRECTION_LEFT = "left"
}