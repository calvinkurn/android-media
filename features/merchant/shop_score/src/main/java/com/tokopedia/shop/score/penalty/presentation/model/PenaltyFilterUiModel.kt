package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

data class PenaltyFilterUiModel(
        val title: String = "",
        val isDividerVisible: Boolean = false,
        var chipsFilerList: List<ChipsFilterPenaltyUiModel> = listOf()
): BaseFilterPenaltyPage {

    data class ChipsFilterPenaltyUiModel(
        var title: String = "",
        var isSelected: Boolean = false,
        var value: Int = 0
    )

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}