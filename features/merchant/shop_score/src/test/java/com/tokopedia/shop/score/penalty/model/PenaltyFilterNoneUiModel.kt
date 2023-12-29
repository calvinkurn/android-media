package com.tokopedia.shop.score.penalty.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory

class PenaltyFilterNoneUiModel: BaseFilterPenaltyPage {
    override fun type(typeFactory: FilterPenaltyAdapterFactory?): Int {
        return 0
    }
}
