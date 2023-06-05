package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

data class PenaltyFilterDateUiModel(
    val startDate: String,
    val endDate: String,
    var defaultStartDate: String,
    var defaultEndDate: String,
    var completeDate: String
): BaseFilterPenaltyPage {

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
