package com.tokopedia.review.feature.reputationhistory.view.model

data class SellerReputationPenaltyMergeUiModel(
    val baseSellerReputationList: List<BaseSellerReputation> = listOf(),
    val hasNext: String? = null,
    val hasPrev: String? = null
)