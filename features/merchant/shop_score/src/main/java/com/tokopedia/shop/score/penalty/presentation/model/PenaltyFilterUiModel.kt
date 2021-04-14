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
        // temporary
        val value: String = "",
        var isSelected: Boolean = false
    )

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}