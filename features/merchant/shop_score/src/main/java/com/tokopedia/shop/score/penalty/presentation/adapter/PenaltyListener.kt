package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel)
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int)
}