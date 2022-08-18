package com.tokopedia.review.feature.inbox.pending.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant

object ReviewPendingTrackingConstants {
    const val EVENT_ACTION_CLICK_PRODUCT_CARD = "${ReviewTrackingConstant.ACTION_CLICK} - product card"
    const val EVENT_ACTION_CLICK_STAR = "${ReviewTrackingConstant.ACTION_CLICK} - product star rating - %s"
    const val EVENT_LABEL_PENDING = "%s - %s - product_is_incentive_eligible:%s;"
    const val EVENT_LABEL_INCENTIVE = "%s - %s - product_is_incentive_eligible:%s;"
    const val BUSINESS_UNIT = "businessUnit"
    const val PDP_BUSINESS_UNIT = "product detail page"
    const val CURRENT_SITE = "currentSite"
    const val CREDIBILITY_CURRENT_SITE = "tokopediamarketplace"

    const val EVENT_NAME_VALUE_SELECT_CONTENT = "select_content"
    const val EVENT_NAME_VALUE_VIEW_ITEM = "view_item"
    const val EVENT_NAME_CLICK_INBOX_REVIEW = "clickInboxReview"
    const val EVENT_NAME_CLICK_PG = "clickPG"

    const val EVENT_ACTION_VALUE_CLICK_WIDGET_ON_REVIEW_INBOX = "click - ulasan widget on inbox ulasan"
    const val EVENT_ACTION_VALUE_IMPRESSION_WIDGET_ON_REVIEW_INBOX = "impression - ulasan widget on inbox ulasan"
    const val EVENT_ACTION_VALUE_CLICK_CHECK_REVIEW_CONTRIBUTION = "click - cek kontribusi ulasan"
    const val EVENT_ACTION_VALUE_CLICK_HI_REVIEW_COMPETITION_WINNER = "click - hai juara ulasan"

    const val EVENT_CATEGORY_VALUE_REVIEW_PAGE_PENDING_REVIEW = "ulasan page - menunggu diulas"

    const val EVENT_LABEL_VALUE_CAROUSEL_ITEM = "title:%s;"

    const val EVENT_FIELD_EE_PROMOTIONS = "promotions"
    const val EVENT_FIELD_EE_CREATIVE_NAME = "creative_name"
    const val EVENT_FIELD_EE_CREATIVE_SLOT = "creative_slot"
    const val EVENT_FIELD_EE_ITEM_ID = "item_id"
    const val EVENT_FIELD_EE_ITEM_NAME = "item_name"

    const val TRACKER_ID_CLICK_CHECK_REVIEW_CONTRIBUTION = "22559"
    const val TRACKER_ID_CLICK_HI_REVIEW_COMPETITION_WINNER = "33902"
}