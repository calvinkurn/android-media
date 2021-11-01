package com.tokopedia.review.feature.reputationhistory.view.model

import com.tokopedia.review.feature.reputationhistory.view.adapter.ReputationPenaltyTypeFactory

data class ReputationShopUiModel(
    val reputationScore: String = "", val badgeReputationUrl: String = ""
) : BaseSellerReputation {
    override fun type(typeFactory: ReputationPenaltyTypeFactory): Int {
        return typeFactory.type(this)
    }
}