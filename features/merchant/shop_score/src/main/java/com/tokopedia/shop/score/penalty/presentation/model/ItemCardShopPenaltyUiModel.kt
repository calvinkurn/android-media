package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemCardShopPenaltyUiModel(val totalPenalty: Int = 0,
                                      val deductionPoints: Int = 0,
                                      val hasPenalty: Boolean = false): BasePenaltyPage {
    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}