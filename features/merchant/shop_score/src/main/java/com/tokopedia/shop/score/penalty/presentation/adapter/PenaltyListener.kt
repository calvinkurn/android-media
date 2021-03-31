package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.sortfilter.SortFilterItem

interface FilterPenaltyListener {
    fun onDateClick()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem, position: Int)
    fun onParentSortFilterClick()
}

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick()
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int)
}