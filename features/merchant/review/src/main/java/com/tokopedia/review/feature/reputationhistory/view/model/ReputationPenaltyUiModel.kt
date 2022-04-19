package com.tokopedia.review.feature.reputationhistory.view.model

import com.tokopedia.review.feature.reputationhistory.view.adapter.ReputationPenaltyTypeFactory

data class ReputationPenaltyUiModel(
    val date: String = "", val invoice: String = "",
    val penaltyScore: String = "", val description: String = ""
) : BaseSellerReputation {
    override fun type(typeFactory: ReputationPenaltyTypeFactory): Int {
        return typeFactory.type(this)
    }
}