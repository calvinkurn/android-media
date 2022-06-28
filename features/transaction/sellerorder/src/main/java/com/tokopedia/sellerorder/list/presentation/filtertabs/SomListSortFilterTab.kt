package com.tokopedia.sellerorder.list.presentation.filtertabs

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify


class SomListSortFilterTab(
    private val sortFilter: SortFilter,
    private val listener: SomListSortFilterTabClickListener
) {

    private var filterItems: ArrayList<SortFilterItem> = arrayListOf()
    private var somListFilterUiModel: SomListFilterUiModel? = null

    init {
        sortFilter.chipItems = arrayListOf()
        setupParentFilter()
    }

    private fun updateTabs(quickFilterList: List<SomListFilterUiModel.QuickFilter>) {
        val isAnyDifference = checkDiff(quickFilterList)
        if (isAnyDifference) {
            recreateTabs(quickFilterList)
        } else {
            quickFilterList.forEach { orderType ->
                filterItems.find {
                    it.title == orderType.name
                }?.apply {
                    listener = { onTabClicked(this, orderType) }
                    title = orderType.name
                    type = if (orderType.isChecked) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                }
            }
        }
    }

    private fun recreateTabs(quickFilterList: List<SomListFilterUiModel.QuickFilter>) {
        filterItems = ArrayList(quickFilterList.map { createNewTabs(it) })
        sortFilter.chipItems?.clear()
        sortFilter.addItem(filterItems)
    }

    private fun checkDiff(quickFilterList: List<SomListFilterUiModel.QuickFilter>): Boolean {
        quickFilterList.forEach { orderType ->
            filterItems.find { it.title == orderType.name } ?: return true
        }
        return false
    }

    private fun createNewTabs(quickFilter: SomListFilterUiModel.QuickFilter): SortFilterItem {
        return SortFilterItem(quickFilter.name).apply {
            listener = { onTabClicked(this, quickFilter) }
            type = if (quickFilter.isChecked) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        }
    }

    private fun onTabClicked(sortFilterItem: SortFilterItem, quickFilter: SomListFilterUiModel.QuickFilter) {
        sortFilterItem.toggleSelected()
        quickFilter.isChecked = sortFilterItem.type == ChipsUnify.TYPE_SELECTED
        listener.onTabClicked(quickFilter, true)
    }

    fun updateCounterSortFilter(
        filterDate: String = "",
        somFilterUiModelList: List<SomFilterUiModel>
    ) {
        var count = 0
        somFilterUiModelList.forEach {
            if (it.nameFilter != SomConsts.FILTER_STATUS_ORDER) {
                it.somFilterData.forEach { somFilter ->
                    if (somFilter.isSelected) count++
                }
            }
        }
        if (filterDate.isNotBlank()) count += 1
    }

    fun show(somListFilterUiModel: SomListFilterUiModel) {
        this.somListFilterUiModel = somListFilterUiModel
        updateTabs(somListFilterUiModel.quickFilterList)
        sortFilter.show()
    }

    private fun setupParentFilter() {
        sortFilter.parentListener = { listener.onParentSortFilterClicked() }
    }

    fun isFilterApplied(): Boolean {
        return sortFilter.indicatorCounter.isMoreThanZero()
    }

    fun isEmpty(): Boolean {
        return filterItems.isEmpty()
    }

    interface SomListSortFilterTabClickListener {
        fun onParentSortFilterClicked()
        fun onTabClicked(quickFilter: SomListFilterUiModel.QuickFilter, shouldScrollToTop: Boolean, fromClickTab: Boolean = true)
    }
}