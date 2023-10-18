package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage

data class PenaltyDataWrapper(
    var penaltyVisitableList: Triple<List<BasePenaltyPage>, Boolean, Boolean> = Triple(
        listOf(),
        second = false,
        third = false
    ),
    var penaltyFilterList: List<BaseFilterPenaltyPage> = listOf()
)
