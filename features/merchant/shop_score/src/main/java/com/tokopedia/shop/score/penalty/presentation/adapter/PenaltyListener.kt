package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.sortfilter.SortFilterItem

interface FilterPenaltyListener {
    fun onDateClick()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem, position: Int)
    fun onParentSortFilterClick()
}

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick(statusPenalty: String)
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int)
}

interface ItemCardShopPenaltyListener {
    fun onMoreInfoHelpPenaltyClicked()
}