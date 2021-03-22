package com.tokopedia.sellerorder.list.presentation.filtertabs

import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_STATUS_COMPLAINT
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_NEW_ORDER
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify


class SomListSortFilterTab(
        private val sortFilter: SortFilter,
        private val listener: SomListSortFilterTabClickListener
) {

    private var selectedTab: SomListFilterUiModel.Status? = null
    private var filterItems: ArrayList<SortFilterItem> = arrayListOf()
    private var somListFilterUiModel: SomListFilterUiModel? = null
    private var somFilterUiModelList: MutableList<SomFilterUiModel> = mutableListOf()
    private var selectedCount: Int = 0
    var isStatusFilterAppliedFromAdvancedFilter: Boolean = false

    init {
        sortFilter.chipItems = arrayListOf()
        sortFilter.indicatorNotifView?.viewTreeObserver?.addOnPreDrawListener {
            val count = selectedCount + if (selectedTab != null && selectedTab?.key != SomConsts.STATUS_ALL_ORDER) 1 else 0
            if (count != sortFilter.indicatorCounter) {
                sortFilter.indicatorCounter = count
                return@addOnPreDrawListener false
            }
            true
        }
        selectParentFilter()
    }

    private fun updateTabs(statusList: List<SomListFilterUiModel.Status>) {
        val isAnyDifference = checkDiff(statusList)
        if (isAnyDifference) {
            recreateTabs(statusList)
        } else {
            statusList.filter { it.key != SomConsts.STATUS_ALL_ORDER }
                    .forEach { statusFilter ->
                        filterItems.find {
                            it.title.contains(statusFilter.status)
                        }?.apply {
                            title = composeTabTitle(statusFilter.key, statusFilter.status, statusFilter.amount)
                            type = if (statusFilter.isChecked) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
                        }
                    }
        }
        updateSelectedTab(statusList.find { it.isChecked })
    }

    private fun recreateTabs(statusList: List<SomListFilterUiModel.Status>) {
        filterItems = ArrayList(statusList.filter { it.key != SomConsts.STATUS_ALL_ORDER }.map { createNewTabs(it) })
        sortFilter.chipItems?.clear()
        sortFilter.addItem(filterItems)
    }

    private fun checkDiff(statusList: List<SomListFilterUiModel.Status>): Boolean {
        statusList.filter { it.key != SomConsts.STATUS_ALL_ORDER }
                .forEach { statusFilter ->
                    filterItems.find {
                        it.title.contains(statusFilter.status)
                    } ?: return true
                }
        return false
    }

    private fun createNewTabs(statusFilter: SomListFilterUiModel.Status): SortFilterItem {
        return SortFilterItem(composeTabTitle(statusFilter.key, statusFilter.status, statusFilter.amount)).apply {
            listener = { onTabClicked(this, statusFilter) }
            type = if (statusFilter.isChecked) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        }
    }

    private fun composeTabTitle(key: String, status: String, amount: Int): String {
        return when (key) {
            STATUS_NEW_ORDER, KEY_CONFIRM_SHIPPING, KEY_STATUS_COMPLAINT -> "$status${" ($amount)".takeIf { amount > 0 } ?: ""}"
            else -> status
        }
    }

    private fun onTabClicked(sortFilterItem: SortFilterItem, status: SomListFilterUiModel.Status) {
        isStatusFilterAppliedFromAdvancedFilter = false
        status.isChecked = if (sortFilterItem.type == ChipsUnify.TYPE_NORMAL) {
            sortFilter.chipItems?.onEach { if (it.type == ChipsUnify.TYPE_SELECTED) it.type = ChipsUnify.TYPE_NORMAL }
            selectTab(status)
            true
        } else {
            selectedTab = null
            sortFilterItem.toggleSelected()
            false
        }
        listener.onTabClicked(status, true)
    }

    private fun updateCounter(count: Int) {
        sortFilter.indicatorCounter = count + if (selectedTab != null && selectedTab?.key != SomConsts.STATUS_ALL_ORDER) 1 else 0
    }

    fun updateCounterSortFilter(filterDate: String = "") {
        var count = 0
        somFilterUiModelList.forEach {
            if (it.nameFilter != SomConsts.FILTER_STATUS_ORDER) {
                it.somFilterData.forEach { somFilter ->
                    if (somFilter.isSelected) count++
                }
            }
        }
        if (filterDate.isNotBlank()) count += 1
        selectedCount = count
    }

    fun show(somListFilterUiModel: SomListFilterUiModel) {
        this.somListFilterUiModel = somListFilterUiModel
        updateTabs(somListFilterUiModel.statusList)
        sortFilter.show()
        updateCounter(selectedCount)
    }

    fun selectTab(status: SomListFilterUiModel.Status) {
        filterItems.filter { !it.title.contains(status.status) }.forEach {
            if (it.type == ChipsUnify.TYPE_SELECTED) {
                it.type = ChipsUnify.TYPE_NORMAL
            }
        }
        filterItems.find { it.title.contains(status.status) }?.apply {
            if (type != ChipsUnify.TYPE_SELECTED) type = ChipsUnify.TYPE_SELECTED
        }
        selectedTab = status
        updateCounter(selectedCount)
    }

    private fun updateSelectedTab(status: SomListFilterUiModel.Status?) {
        selectedTab = status
    }

    private fun selectParentFilter() {
        sortFilter.apply {
            parentListener = {
                listener.onParentSortFilterClicked()
            }
        }
    }

    fun addCounter(n: Int) {
        selectedCount += n
        updateCounter(selectedCount)
    }

    fun shouldShowBulkAction() = (selectedTab?.key == STATUS_NEW_ORDER || selectedTab?.key == KEY_CONFIRM_SHIPPING) && GlobalConfig.isSellerApp()
    fun isNewOrderFilterSelected(): Boolean = selectedTab?.key == STATUS_NEW_ORDER
    fun getSelectedFilterOrderCount(): Int = selectedTab?.amount.orZero()
    fun getSelectedFilterStatus(): String = selectedTab?.key.orEmpty()
    fun getSelectedFilterStatusName(): String = selectedTab?.status.orEmpty()

    fun getSelectedFilterKeys() = somFilterUiModelList.filter { it.nameFilter != SomConsts.FILTER_STATUS_ORDER }
            .map { it.somFilterData.filter { it.isSelected }.map { it.key } }.flatten()

    fun getSomFilterUi() = somFilterUiModelList

    fun updateSomListFilterUi(somFilterUiModelList: List<SomFilterUiModel>) {
        this.somFilterUiModelList.clear()
        this.somFilterUiModelList.addAll(somFilterUiModelList)
        this.isStatusFilterAppliedFromAdvancedFilter = isStatusFilterSelected()
    }

    fun isSortByAppliedManually(): Boolean {
        return this.somFilterUiModelList.any {
            it.nameFilter == SomConsts.FILTER_SORT && it.somFilterData.any {
                it.isSelected
            }
        }
    }

    private fun isStatusFilterSelected(): Boolean {
        return this.somFilterUiModelList.any {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER && it.somFilterData.any { chips ->
                chips.isSelected
            }
        }
    }

    fun isFilterApplied(): Boolean {
        return (sortFilter.indicatorCounter - 1.takeIf { selectedTab != null }.orZero()) != 0
    }

    fun clear() {
        updateSomListFilterUi(emptyList())
    }

    fun unselectCurrentStatusFilter() {
        val currentSelectedTab = selectedTab
        if (currentSelectedTab != null) {
            filterItems.find { it.title.contains(currentSelectedTab.status) }?.let {
                onTabClicked(it, currentSelectedTab)
            }
        }
    }

    fun isEmpty(): Boolean {
        return filterItems.isEmpty()
    }

    fun getAllStatusCodes(): List<String> {
        return somListFilterUiModel?.statusList?.find { it.key == SomConsts.STATUS_ALL_ORDER }
                ?.id?.map { it.toString() }.orEmpty()
    }

    interface SomListSortFilterTabClickListener {
        fun onParentSortFilterClicked()
        fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean, refreshFilter: Boolean = true)
    }
}