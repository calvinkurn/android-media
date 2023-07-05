package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

data class PenaltyFilterDateUiModel(
    var startDate: String,
    var endDate: String,
    var defaultStartDate: String,
    var defaultEndDate: String,
    var initialStartDate: String,
    var initialEndDate: String,
    var completeDate: String
): BaseFilterPenaltyPage {

    override fun type(typeFactory: FilterPenaltyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
