package com.tokopedia.shop.score.penalty.presentation.adapter

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick(statusPenalty: String)
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int)
}

interface PenaltyGlobalErrorListener {
    fun onBtnErrorClicked()
}