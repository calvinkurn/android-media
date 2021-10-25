package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

/**
 * @author by nisie on 8/30/17.
 */
data class RevieweeBadgeCustomerUiModel(
    var positive: Int = 0,
    var neutral: Int = 0,
    var negative: Int = 0,
    var positivePercentage: String = "",
    var noReputation: Int = 0
)