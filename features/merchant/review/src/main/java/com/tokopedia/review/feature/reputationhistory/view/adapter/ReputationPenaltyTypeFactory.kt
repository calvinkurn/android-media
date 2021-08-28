package com.tokopedia.review.feature.reputationhistory.view.adapter

import com.tokopedia.review.feature.reputationhistory.view.model.ReputationDateUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel

interface ReputationPenaltyTypeFactory {
    fun type(reputationDateUiModel: ReputationDateUiModel): Int
    fun type(reputationShopUiModel: ReputationShopUiModel): Int
    fun type(reputationPenaltyUiModel: ReputationPenaltyUiModel): Int
}