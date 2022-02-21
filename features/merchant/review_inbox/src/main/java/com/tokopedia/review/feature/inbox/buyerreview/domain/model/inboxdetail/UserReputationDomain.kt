package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class UserReputationDomain constructor(
    val positive: Int = 0, 
    val neutral: Int = 0, 
    val negative: Int = 0,
    val positivePercentage: String = "", 
    val noReputation: Int = 0
)