package com.tokopedia.shop.score.penalty.presentation.model.filtertypes

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyTypesTypeFactory

data class ItemPenaltyFilterTypesChecklistUiModel(
    var isSelected: Boolean,
    val title: String,
    val filterId: Int
): Visitable<FilterPenaltyTypesTypeFactory> {

    override fun type(typeFactory: FilterPenaltyTypesTypeFactory): Int {
        return typeFactory.type(this)
    }
}
