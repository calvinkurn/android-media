package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.sortfilter.SortFilterItem

interface FilterPenaltyListener {
    fun onDateClick()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem, position: Int)
    fun onParentSortFilterClick()
}