package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

data class PenaltyFilterDateUiModel(
    val startDate: String,
    val endDate: String,
    val defaultStartDate: String,
    val defaultEndDate: String,
    val completeDate: String
): BaseFilterPenaltyPage {

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
