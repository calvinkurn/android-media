package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

object ItemPenaltyTickerUiModel: BasePenaltyPage {

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
