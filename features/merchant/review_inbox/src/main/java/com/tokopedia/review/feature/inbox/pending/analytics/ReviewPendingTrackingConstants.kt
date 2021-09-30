package com.tokopedia.review.feature.inbox.pending.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant

object ReviewPendingTrackingConstants {
    const val EVENT_ACTION_CLICK_PRODUCT_CARD = "${ReviewTrackingConstant.ACTION_CLICK} - product card"
    const val EVENT_ACTION_CLICK_STAR = "${ReviewTrackingConstant.ACTION_CLICK} - product star rating - %s"
    const val EVENT_LABEL_PENDING = "%s - %s - product_is_incentive_eligible:%s;"
    const val EVENT_LABEL_INCENTIVE = "%s - %s - product_is_incentive_eligible:%s;"
    const val EVENT_CLICK_INBOX_REVIEW = "clickInboxReview"
    const val EVENT_ACTION_CLICK_CREDIBILITY = "click - cek kontribusi ulasan"
    const val BUSINESS_UNIT = "businessUnit"
    const val PDP_BUSINESS_UNIT = "product detail page"
    const val CURRENT_SITE = "currentSite"
    const val CREDIBILITY_CURRENT_SITE = "tokopediamarketplace"
}