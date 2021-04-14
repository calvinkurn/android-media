package com.tokopedia.shop.score.penalty.presentation.model

data class PenaltyDataWrapper(
        var cardShopPenaltyUiModel: ItemCardShopPenaltyUiModel? = null,
        var itemDetailPenaltyFilterUiModel: ItemDetailPenaltyFilterUiModel? = null,
        var itemPenaltyUiModel: List<ItemPenaltyUiModel> = listOf()
)