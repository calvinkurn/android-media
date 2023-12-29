package com.tokopedia.shop.score.penalty.presentation.model.calculation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationTypeFactory

data class ItemPenaltyCalculationFormulaUiModel(
    val ongoingPenalty: Int,
    val totalOrder: Long,
    val percentageResult: String,
    val pointResult: Int
): Visitable<PenaltyCalculationTypeFactory> {

    override fun type(typeFactory: PenaltyCalculationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
