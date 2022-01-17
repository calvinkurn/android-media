package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain

/**
 * @author by nisie on 8/23/17.
 */
class ShopReputationDomain constructor(
    val tooltip: String = "",
    val reputationScore: String = "",
    val score: Int = 0,
    val minBadgeScore: Int = 0 ,
    val reputationBadgeUrl: String = "",
    val reputationBadge: ReputationBadgeDomain = ReputationBadgeDomain()
)