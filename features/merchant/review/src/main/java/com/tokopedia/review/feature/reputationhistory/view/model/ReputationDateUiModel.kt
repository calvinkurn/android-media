package com.tokopedia.review.feature.reputationhistory.view.model

import com.tokopedia.review.feature.reputationhistory.view.adapter.ReputationPenaltyTypeFactory

data class ReputationDateUiModel(var startDate: Long = 0, var endDate: Long = 0): BaseSellerReputation {
    override fun type(typeFactory: ReputationPenaltyTypeFactory): Int {
        return typeFactory.type(this)
    }
}