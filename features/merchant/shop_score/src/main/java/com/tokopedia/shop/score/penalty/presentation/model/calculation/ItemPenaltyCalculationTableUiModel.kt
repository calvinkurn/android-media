package com.tokopedia.shop.score.penalty.presentation.model.calculation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationTypeFactory

data class ItemPenaltyCalculationTableUiModel(
    val conversionList: List<Triple<String, Long, Boolean>>
): Visitable<PenaltyCalculationTypeFactory> {

    override fun type(typeFactory: PenaltyCalculationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
