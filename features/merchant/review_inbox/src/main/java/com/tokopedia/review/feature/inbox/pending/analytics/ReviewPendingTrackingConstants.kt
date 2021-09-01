package com.tokopedia.review.feature.inbox.pending.analytics

import com.tokopedia.review.common.analytics.ReviewTrackingConstant

object ReviewPendingTrackingConstants {
    const val EVENT_ACTION_CLICK_PRODUCT_CARD = "${ReviewTrackingConstant.ACTION_CLICK} - product card"
    const val EVENT_ACTION_CLICK_STAR = "${ReviewTrackingConstant.ACTION_CLICK} - product star rating - %s"
    const val EVENT_LABEL_PENDING = "%s - %s - product_is_incentive_eligible:%s;"
    const val EVENT_LABEL_INCENTIVE = "%s - %s - product_is_incentive_eligible:%s;"
}