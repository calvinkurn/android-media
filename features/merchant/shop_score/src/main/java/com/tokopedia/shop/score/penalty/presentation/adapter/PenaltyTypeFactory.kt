package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

interface PenaltyTypeFactory {
    fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int
}