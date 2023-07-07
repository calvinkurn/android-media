package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

class PenaltyFilterUiModel(
    val title: String = "",
    val isDividerVisible: Boolean = false,
    var chipsFilterList: List<ChipsFilterPenaltyUiModel> = listOf(),
    var shownFilterList: List<ChipsFilterPenaltyUiModel> = listOf()
) : BaseFilterPenaltyPage {

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
