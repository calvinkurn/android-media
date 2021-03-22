package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltyUiModel(val statusPenalty: String = "",
                              val endDate: String = "",
                              val statusDate: String = "",
                              val transactionPenalty: String = "",
                              val descPenalty: String = "",
                              val colorPenalty: String = ""
) : BasePenaltyPage {
    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}