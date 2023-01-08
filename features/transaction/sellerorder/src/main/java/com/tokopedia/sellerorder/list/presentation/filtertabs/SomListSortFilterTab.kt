package com.tokopedia.sellerorder.list.presentation.filtertabs

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUtil
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify


class SomListSortFilterTab(
    private val sortFilter: SortFilter,
    private val listener: SomListSortFilterTabClickListener
) {

    private var filterItems: ArrayList<SortFilterItem> = arrayListOf()
    private var selectedCount: Int = Int.ZERO

    init {
        sortFilter.chipItems = arrayListOf()
        sortFilter.indicatorNotifView?.viewTreeObserver?.addOnPreDrawListener {
            if (selectedCount != sortFilter.indicatorCounter) {
                sortFilter.indicatorCounter = selectedCount
                return@addOnPreDrawListener false
            }
            true
        }
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
        quickFilter.isChecked = if (sortFilterItem.type == ChipsUnify.TYPE_SELECTED) {
            selectedCount++
            true
        } else {
            selectedCount--
            false
        }
        updateCounter()
        listener.onTabClicked(quickFilter, true)
    }

    private fun setupParentFilter() {
        sortFilter.parentListener = { listener.onParentSortFilterClicked() }
    }

    private fun updateCounter() {
        sortFilter.indicatorCounter = selectedCount
    }

    fun updateCounterSortFilter(
        somFilterUiModelList: List<SomFilterUiModel>,
        somListFilterUiModel: SomListFilterUiModel,
        somListGetOrderListParam: SomListGetOrderListParam
    ) {
        var count = 0
        if (somFilterUiModelList.isNotEmpty()) {
            val selectedOrderStatusFilter = somFilterUiModelList.find {
                it.nameFilter == SomConsts.FILTER_STATUS_ORDER
            }?.somFilterData?.firstOrNull { it.isSelected }?.key ?: SomConsts.STATUS_ALL_ORDER
            val defaultSortByFilter = SomFilterUtil.getDefaultSortBy(selectedOrderStatusFilter)
            somFilterUiModelList.forEach {
                if (it.nameFilter == SomConsts.FILTER_SORT) {
                    it.somFilterData.forEach {
                        if (it.isSelected && it.id != defaultSortByFilter) count++
                    }
                } else {
                    it.somFilterData.forEach {
                        if (it.isSelected) count++
                    }
                }
            }
        } else {
            val selectedOrderStatusFilter = somListFilterUiModel.statusList.firstOrNull {
                it.isChecked
            }?.key ?: SomConsts.STATUS_ALL_ORDER
            if (selectedOrderStatusFilter != SomConsts.STATUS_ALL_ORDER) count++
            count += filterItems.count { it.type == ChipsUnify.TYPE_SELECTED }
        }
        val defaultDateFilter = SomFilterUtil.getDefaultDateFilter()
        if (somListGetOrderListParam.startDate != defaultDateFilter.first || somListGetOrderListParam.endDate != defaultDateFilter.second) count++
        selectedCount = count
        updateCounter()
    }

    fun show(somListFilterUiModel: SomListFilterUiModel) {
        updateTabs(somListFilterUiModel.quickFilterList)
        sortFilter.show()
    }

    fun isNonStatusOrderFilterApplied(selectedFilterStatus: String?): Boolean {
        val nonAllOrderStatusFilterSelected = selectedFilterStatus?.isNotBlank() == true && selectedFilterStatus != SomConsts.STATUS_ALL_ORDER
        val nonStatusOrderFilterCount = sortFilter.indicatorCounter - 1.takeIf {
            nonAllOrderStatusFilterSelected
        }.orZero()
        return nonStatusOrderFilterCount.isMoreThanZero()
    }

    fun isEmpty(): Boolean {
        return filterItems.isEmpty()
    }

    interface SomListSortFilterTabClickListener {
        fun onParentSortFilterClicked()
        fun onTabClicked(quickFilter: SomListFilterUiModel.QuickFilter, shouldScrollToTop: Boolean)
    }
}