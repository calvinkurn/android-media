package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

/**
 * @author by nisie on 8/30/17.
 */
data class RevieweeBadgeSellerUiModel(
    var tooltip: String = "",
    var reputationScore: String = "",
    var score: Int = 0,
    var minBadgeScore: Int = 0,
    var reputationBadgeUrl: String = "",
    var reputationBadge: ReputationBadgeUiModel = ReputationBadgeUiModel(),
    var isFavorited: Int = -1
)