package com.tokopedia.shop.score.penalty.presentation.model

data class PenaltyDataWrapper(
        var cardShopPenaltyUiModel: ItemCardShopPenaltyUiModel? = null,
        var itemDetailPenaltyFilterUiModel: ItemDetailPenaltyFilterUiModel? = null,
        var itemPenaltyUiModel: Triple<List<ItemPenaltyUiModel>, Boolean, Boolean> = Triple(listOf(), second = false, third = false),
        var penaltyFilterList: List<PenaltyFilterUiModel>? = listOf()
)