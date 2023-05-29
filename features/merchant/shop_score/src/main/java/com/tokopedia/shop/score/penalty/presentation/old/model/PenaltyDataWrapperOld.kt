package com.tokopedia.shop.score.penalty.presentation.old.model

import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage

data class PenaltyDataWrapperOld(
    var penaltyVisitableList: Triple<List<BasePenaltyPage>, Boolean, Boolean> = Triple(
        listOf(),
        second = false,
        third = false
    ),
    var penaltyFilterList: List<PenaltyFilterUiModelOld> = listOf()
)
