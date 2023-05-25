package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltySubsectionUiModel(
    val sectionName: String,
    val date: String
): BasePenaltyPage {

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
