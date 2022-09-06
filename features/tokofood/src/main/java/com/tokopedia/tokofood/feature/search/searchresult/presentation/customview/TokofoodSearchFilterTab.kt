package com.tokopedia.tokofood.feature.search.searchresult.presentation.customview

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class TokofoodSearchFilterTab(
    private val sortFilter: SortFilter,
    private val listener: Listener
) {

    init {
        initSortFilter()
    }

    fun setQuickFilter(items: List<TokofoodSortFilterItemUiModel>) {
        with(sortFilter) {
            val sortFilterItemList = items.mapNotNull(::mapSortFilterUiModelToSortFilterItems)
            sortFilterItems.removeAllViews()
            if (sortFilterItemList.isNotEmpty()) {
                visible()
                sortFilterHorizontalScrollView.scrollX = 0
                addItem(sortFilterItemList as ArrayList)
                chipItems?.forEach {
                    it.updateSelectedRef = { _,_,_,_,_ -> }
                }
                textView?.text = context.getString(com.tokopedia.tokofood.R.string.search_srp_filter_chip_title)
            }
        }
    }

    private fun initSortFilter() {
        sortFilter.gone()
        setSortFilterClickListener()
    }

    private fun setSortFilterClickListener() {
        sortFilter.parentListener = {
            listener.onOpenFullFilterBottomSheet()
        }
    }

    private fun mapSortFilterUiModelToSortFilterItems(uiModel: TokofoodSortFilterItemUiModel): SortFilterItem? {
        return when(uiModel) {
            is TokofoodSortItemUiModel -> uiModel.getSortFilterItem()
            is TokofoodFilterItemUiModel -> uiModel.getSortFilterItem()
            else -> null
        }
    }

    private fun TokofoodSortItemUiModel.getSortFilterItem(): SortFilterItem? {
        return sortFilterItem.takeIf { sortList.isNotEmpty() }?.also { item ->
            if (sortList.size > 1) {
                item.listener = {
                    listener.onOpenQuickFilterBottomSheet(sortList)
                }
                item.chevronListener = {
                    listener.onOpenQuickFilterBottomSheet(sortList)
                }
            } else {
                val sort = sortList.firstOrNull()
                if (sort == null) {
                    item.listener = {}
                } else {
                    item.listener =  {
                        toggleActiveChip(item, sort)
                    }
                }
            }
            item.setActive(selectedSort != null)
        }
    }

    private fun TokofoodFilterItemUiModel.getSortFilterItem(): SortFilterItem? {
        return sortFilterItem.takeIf { filter.options.isNotEmpty() }?.also { item ->
            if (filter.options.size > Int.ONE) {
                item.listener = {
                    listener.onOpenQuickFilterBottomSheet(filter)
                }
                item.chevronListener = {
                    listener.onOpenQuickFilterBottomSheet(filter)
                }
            } else {
                val filterOption = filter.options.firstOrNull()
                if (filterOption == null) {
                    item.listener = {}
                } else {
                    item.listener = {
                        toggleActiveChip(item, filter)
                    }
                }
            }
            item.setActive(filter)
        }
    }

    private fun SortFilterItem.setActive(filter: Filter) {
        val isActive = filter.options.any { it.inputState == true.toString() }
        type =
            if (isActive) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
    }

    private fun SortFilterItem.setActive(isSortSelected: Boolean) {
        type =
            if (isSortSelected) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
    }

    private fun toggleActiveChip(sortFilterItem: SortFilterItem, sort: Sort) {
        val isSelected: Boolean = sortFilterItem.type == ChipsUnify.TYPE_NORMAL
        listener.onSelectSortChip(sort, isSelected)
    }

    private fun toggleActiveChip(sortFilterItem: SortFilterItem, filter: Filter) {
        val isSelected: Boolean = sortFilterItem.type == ChipsUnify.TYPE_NORMAL
        filter.run {
            options.firstOrNull()?.inputState = isSelected.toString()
        }
        listener.onSelectFilterChip(filter)
    }

    interface Listener {
        fun onOpenFullFilterBottomSheet()
        fun onOpenQuickFilterBottomSheet(sortList: List<Sort>)
        fun onOpenQuickFilterBottomSheet(filter: Filter)
        fun onSelectSortChip(sort: Sort, isSelected: Boolean)
        fun onSelectFilterChip(filter: Filter)
    }

}