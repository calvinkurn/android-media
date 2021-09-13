package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
class RevieweeBadgeSellerDomain constructor(
    val tooltip: String?, val reputationScore: String?, val score: Int,
    val minBadgeScore: Int, val reputationBadgeUrl: String?,
    val reputationBadge: ReputationBadgeDomain
) {
    var isFavorited: Int = 0

}