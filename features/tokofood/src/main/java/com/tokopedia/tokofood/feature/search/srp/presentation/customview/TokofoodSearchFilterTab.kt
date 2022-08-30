package com.tokopedia.tokofood.feature.search.srp.presentation.customview

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodSortItemUiModel

class TokofoodSearchFilterTab(
    private val sortFilter: SortFilter,
    private val filterController: FilterController,
    private val listener: Listener
) {
    init {
        initSortFilter()
    }

    fun show() {

    }

    fun setQuickFilter(items: List<TokofoodSortItemUiModel>) {
        with(sortFilter) {
            val sortFilterItemList = items.mapNotNull(::mapSortFilterUiModelToSortFilterItems)
            sortFilterItems.removeAllViews()
            visible()
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(sortFilterItemList as ArrayList)
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
                item.chevronListener = {}
                val sort = sortList.firstOrNull()
                if (sort == null) {
                    item.listener = {}
                } else {
                    item.listener =  {
                        listener.onSelectSortChip(sort)
                    }
                }
            }
        }
    }

    private fun TokofoodFilterItemUiModel.getSortFilterItem(): SortFilterItem? {
        return sortFilterItem.takeIf { filter.options.isNotEmpty() }?.also { item ->
            if (filter.options.size > 1) {
                item.listener = {
                    listener.onOpenQuickFilterBottomSheet(filter)
                }
                item.chevronListener = {
                    listener.onOpenQuickFilterBottomSheet(filter)
                }
            } else {
                item.chevronListener = {}
                val filter = filter.options.firstOrNull()
                if (filter == null) {
                    item.listener = {}
                } else {
                    item.listener = {
                        listener.onSelectFilterChip(filter)
                    }
                }
            }
        }
    }

    interface Listener {
        fun onOpenFullFilterBottomSheet()
        fun onOpenQuickFilterBottomSheet(sortList: List<Sort>)
        fun onOpenQuickFilterBottomSheet(filter: Filter)
        fun onSelectSortChip(sort: Sort)
        fun onSelectFilterChip(option: Option)
    }

}