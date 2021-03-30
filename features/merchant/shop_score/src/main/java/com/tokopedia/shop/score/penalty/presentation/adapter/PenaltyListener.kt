package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.sortfilter.SortFilterItem

interface FilterPenaltyListener {
    fun onDateClick()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem, position: Int)
    fun onParentSortFilterClick()
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, position: Int)
}