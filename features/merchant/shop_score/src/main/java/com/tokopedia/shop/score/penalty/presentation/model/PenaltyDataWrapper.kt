package com.tokopedia.shop.score.penalty.presentation.model

data class PenaltyDataWrapper(
        var penaltyVisitableList: Triple<List<BasePenaltyPage>, Boolean, Boolean> = Triple(listOf(), second = false, third = false),
        var penaltyFilterList: List<PenaltyFilterUiModel>? = listOf()
)