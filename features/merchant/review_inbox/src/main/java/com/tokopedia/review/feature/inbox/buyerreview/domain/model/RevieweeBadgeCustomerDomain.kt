package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/16/17.
 */
class RevieweeBadgeCustomerDomain constructor(
    val positive: Int, val neutral: Int,
    val negative: Int, val positivePercentage: String?,
    val noReputation: Int
)