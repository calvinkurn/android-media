package com.tokopedia.shop.score.penalty.presentation.model.calculation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationTypeFactory

data class ItemPenaltyCalculationSubtitleUiModel(
    val title: String,
    val desc: String = String.EMPTY
): Visitable<PenaltyCalculationTypeFactory> {

    override fun type(typeFactory: PenaltyCalculationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
