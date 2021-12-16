package com.tokopedia.review.feature.createreputation.analytics

object CreateReviewTrackingConstants {
    const val EVENT_ACTION_CLICK_COLLAPSE_TEXT_BOX = "click - collapse textbox"
    const val EVENT_ACTION_CLICK_COMPLETE_REVIEW_FIRST = "click - lengkapi ulasan dulu on %s"
    const val EVENT_ACTION_CLICK_CONTINUE_IN_DIALOG = "click - lanjut tulis ulasan on %s"
    const val EVENT_ACTION_CLICK_EXPAND_TEXTBOX = "click - expand textbox"
    const val EVENT_ACTION_CLICK_INCENTIVES_TICKER = "click - pelajari on ovo incentives ticker"
    const val EVENT_ACTION_CLICK_ONGOING_CHALLENGE_TICKER = "click - pelajari on tantangan ulasan ticker"
    const val EVENT_ACTION_CLICK_LEAVE_PAGE = "click - ya keluar on %s"
    const val EVENT_ACTION_CLICK_SEND_NOW = "click - kirim sekarang aja on %s"
    const val EVENT_ACTION_CLICK_SEND_RATING_OPTION = "click - kirim rating on pop up"
    const val EVENT_ACTION_CLICK_STAY_OPTION = "click - tetap disini on pop up"
    const val EVENT_ACTION_CLICK_SUBMIT = "click - kirim ulasan produk"
    const val EVENT_ACTION_CLICK_TEMPLATE = "click - review template"
    const val EVENT_ACTION_DISMISS_FORM = "click - dismiss create review form"
    const val EVENT_ACTION_VIEW_DIALOG = "view - pop up %s"
    const val EVENT_ACTION_VIEW_SEND_RATING_DIALOG = "view - mau kirim rating aja pop up"
    const val EVENT_ACTION_VIEW_SMILEY = "view - smiley components"
    const val EVENT_ACTION_VIEW_TEMPLATE = "view - review template"
    const val EVENT_ACTION_VIEW_THANK_YOU_BOTTOM_SHEET = "view - terimakasih untuk ulasanmu pop up"
    const val EVENT_ACTION_VIEW_POST_SUBMIT_REVIEW_BOTTOM_SHEET = "view - bottomsheet postsubmit review"
    const val EVENT_ACTION_VIEW_UNSAVED_DIALOG = "view - ulasanmu nanti hilang pop up"
    const val EVENT_ACTION_CLICK_BAD_RATING_REASON = "click - bad review template checkbox"
    const val EVENT_ACTION_IMPRESS_BAD_RATING_REASON = "impression - bad review template checkbox"
    const val EVENT_ACTION_CLICK_THANK_YOU_BOTTOM_SHEET_BUTTON = "click - cta on bottomsheet postsubmit review"

    const val EVENT_CATEGORY = "product review detail page"
    const val EVENT_CATEGORY_REVIEW_BOTTOM_SHEET =
        "product review detail page - create review form bottomsheet"

    const val EVENT_LABEL_ORDER_ID_PRODUCT_ID = "order id:%s; product id:%s;"
    const val EMPTY_LABEL = ""
    const val EVENT_LABEL_VIEW_DIALOG =
        "pop up title:%s;reputation_id:%s;order_id:%s;product_id:%s;"
    const val EVENT_LABEL_PENDING_INCENTIVE_QUEUE = "has pending incentive review queue:%s;"
    const val EVENT_LABEL_CLICK_TEMPLATE = "template:%s;reputation_id:%s;order_id:%s;product_id:%s;"
    const val EVENT_LABEL_CLICK_SUBMIT =
        "order_id : %s - product_id : %s - star : %d - ulasan : %s - review_char : %d - gambar : %d - anonim : %s - feedback_is_incentive_eligible : %s - is_template_available : %s - count_template_used : %d"
    const val EVENT_LABEL_VIEW_TEMPLATE = "template_impressed:%d"
    const val EVENT_LABEL_VIEW_THANK_YOU_BOTTOM_SHEET =
        "pop up title:%s;reputation_id:%s;order_id:%s;product_id:%s;feedback_id:%s;"
    const val EVENT_LABEL_VIEW_POST_SUBMIT_REVIEW_BOTTOM_SHEET =
        "pop_up_title:%s;reputation_id:%s;order_id:%s;product_id:%s;feedback_id:%s;"
    const val EVENT_LABEL_CLICK_POST_SUBMIT_REVIEW_BOTTOM_SHEET_CTA =
        "pop_up_title:%s;cta:%s;reputation_id:%s;order_id:%s;product_id:%s;feedback_id:%s;"
    const val EVENT_LABEL_VIEW_INCENTIVES_TICKER =
        "message:%s;reputation_id:%s;order_id:%s;product_id:%s;"
    const val EVENT_LABEL_VIEW_ONGOING_CHALLENGE_TICKER =
        "reputation_id:%s;order_id:%s;product_id:%s;"
    const val EVENT_LABEL_CLICK_BAD_RATING_REASON = "order_id:%s;product_id:%s;reason:%s;is_active:%s;"
    const val EVENT_LABEL_IMPRESS_BAD_RATING_REASON = "order_id:%s;product_id:%s;"

    const val SCREEN_NAME = "/create-review-form"
    const val SCREEN_NAME_BOTTOM_SHEET = "create-review-form"

    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_PRODUCT_ID = "productId"
    const val KEY_DEEPLINK = "deeplink"
    const val KEY_ECOMMERCE = "ecommerce"
    const val KEY_PROMOTIONS = "promotions"
    const val KEY_ID = "id"
    const val KEY_CREATIVE = "creative"
    const val KEY_NAME = "name"
    const val KEY_POSITION = "position"

    const val BUSINESS_UNIT = "product detail page"
    const val CURRENT_SITE = "tokopediamarketplace"

    const val EVENT_PROMO_VIEW = "promoView"
    const val BAD_RATING_REASON_IMPRESSION_ENHANCED_ECOMMERCE_ID = "reason:%s;"

    const val EVENT_KEY_EE_CREATIVE_NAME = "creative_name"
    const val EVENT_KEY_EE_CREATIVE_SLOT = "creative_slot"
    const val EVENT_KEY_EE_ITEM_ID = "item_id"
    const val EVENT_KEY_EE_ITEM_NAME = "item_name"

    const val EVENT_KEY_EE_PROMOTIONS = "promotions"
}