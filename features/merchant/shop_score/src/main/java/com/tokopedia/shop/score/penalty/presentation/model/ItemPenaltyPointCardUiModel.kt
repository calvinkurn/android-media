package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltySummary
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltyPointCardUiModel(
    val result: ShopScorePenaltySummary.Result,
    val date: String
): BasePenaltyPage {

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
