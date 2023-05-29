package com.tokopedia.shop.score.penalty.presentation.old.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel

data class PenaltyFilterUiModelOld(
    val title: String = "",
    val isDividerVisible: Boolean = false,
    var chipsFilterList: List<ChipsFilterPenaltyUiModel> = listOf()
) : BaseFilterPenaltyPage {

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
