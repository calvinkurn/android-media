package com.tokopedia.sellerorder.list.presentation.filtertabs

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.common.util.SomConsts
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

    companion object {
        private const val SWIPE_TAB_ANIMATION_DELAY = 500L
    }

    private var selectedTab: SomListFilterUiModel.Status? = null
    private var filterItems: ArrayList<SortFilterItem> = arrayListOf()
    private var somListFilterUiModel: SomListFilterUiModel? = null
    private var somFilterUiModelList: MutableList<SomFilterUiModel> = mutableListOf()
    private var selectedCount: Int = 0

    init {
        sortFilter.chipItems = arrayListOf()
        selectParentFilter()
    }

    fun selectTabReset() {
        selectedTab = null
    }

    fun updateTabs(statusList: List<SomListFilterUiModel.Status>) {
        // use old filter items if any
        val filters = statusList.filter { it.key != SomConsts.STATUS_ALL_ORDER }
                .map { statusFilter ->
                    filterItems.find {
                        it.title.contains(statusFilter.status)
                    }?.apply {
                        title = composeTabTitle(statusFilter.status, statusFilter.amount)
                        type = if (statusFilter.isChecked) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
                    } ?: createNewTabs(statusFilter)
                }
        filterItems = ArrayList(filters)
        sortFilter.chipItems.clear()
        sortFilter.addItem(filterItems)
    }

    private fun createNewTabs(statusFilter: SomListFilterUiModel.Status): SortFilterItem {
        return SortFilterItem(composeTabTitle(statusFilter.status, statusFilter.amount)).apply {
            listener = { onTabClicked(this, statusFilter) }
            type = if (statusFilter.isChecked) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        }
    }

    private fun composeTabTitle(status: String, amount: Int): String {
        return "$status${" ($amount)".takeIf { amount > 0 } ?: ""}"
    }

    private fun onTabClicked(sortFilterItem: SortFilterItem, status: SomListFilterUiModel.Status) {
        status.isChecked = if (sortFilterItem.type == ChipsUnify.TYPE_NORMAL) {
            sortFilter.chipItems.onEach { if (it.type == ChipsUnify.TYPE_SELECTED) it.type = ChipsUnify.TYPE_NORMAL }
            selectTab(status)
            true
        } else {
            selectedTab = null
            false
        }
        sortFilterItem.type = ChipsUnify.TYPE_SELECTED
        listener.onTabClicked(status, true)
    }

    private fun updateCounter(count: Int) {
        sortFilter.indicatorCounter = count + if (selectedTab != null && selectedTab?.key != SomConsts.STATUS_ALL_ORDER) 1 else 0
    }

    fun updateCounterSortFilter(filterDate: String = "") {
        var count = 0
        somFilterUiModelList.forEach {
            if(it.nameFilter != SomConsts.FILTER_STATUS_ORDER) {
                it.somFilterData.forEach { somFilter ->
                    if (somFilter.isSelected) count++
                }
            }
        }
        if(filterDate.isNotBlank()) count += 1
        selectedCount = count
    }

    fun show(somListFilterUiModel: SomListFilterUiModel) {
        this.somListFilterUiModel = somListFilterUiModel
        updateTabs(somListFilterUiModel.statusList)
        sortFilter.show()
    }

    fun selectTab(status: SomListFilterUiModel.Status) {
        filterItems.forEach {
            if (!it.title.contains(status.status) && it.type == ChipsUnify.TYPE_SELECTED) {
                it.type = ChipsUnify.TYPE_NORMAL
            }
        }
        filterItems.find { it.title.contains(status.status) }?.apply {
            if (type != ChipsUnify.TYPE_SELECTED) type = ChipsUnify.TYPE_SELECTED
        }
        selectedTab = status
        sortFilter.postDelayed({
            updateCounter(selectedCount)
        }, SWIPE_TAB_ANIMATION_DELAY)
    }

    fun updateCounterFilter() {
        updateCounter(selectedCount)
    }

    private fun selectParentFilter() {
        sortFilter.apply {
            parentListener = {
                listener.onParentSortFilterClicked()
            }
        }
    }

    fun decrementOrderCount() {
        selectedTab?.run {
            amount -= 1
            filterItems.find { it.title.contains(status) }?.title = composeTabTitle(status, amount)
        }
    }

    fun addCounter(n: Int) {
        selectedCount += n
        updateCounter(selectedCount)
    }

    fun shouldShowBulkAction() = selectedTab?.key == STATUS_NEW_ORDER
    fun isNewOrderFilterSelected(): Boolean = selectedTab?.key == STATUS_NEW_ORDER
    fun getSelectedFilterOrderCount(): Int = selectedTab?.amount.orZero()
    fun getSelectedFilterStatus(): String = selectedTab?.key.orEmpty()
    fun getSelectedFilterSatusName(): String = selectedTab?.status.orEmpty()

    fun getSelectedTab() = selectedTab

    fun getSomListFilterUiModel() = somListFilterUiModel

    fun getSomFilterUi() = somFilterUiModelList

    fun updateSomListFilterUi(somFilterUiModelList: List<SomFilterUiModel>) {
        this.somFilterUiModelList.clear()
        this.somFilterUiModelList.addAll(somFilterUiModelList)
    }

    interface SomListSortFilterTabClickListener {
        fun onParentSortFilterClicked()
        fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean)
    }
}