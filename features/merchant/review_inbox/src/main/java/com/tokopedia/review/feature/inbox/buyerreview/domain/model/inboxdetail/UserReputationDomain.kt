package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class UserReputationDomain constructor(
    val positive: Int, val neutral: Int, val negative: Int,
    val positivePercentage: String?, val noReputation: Int
)