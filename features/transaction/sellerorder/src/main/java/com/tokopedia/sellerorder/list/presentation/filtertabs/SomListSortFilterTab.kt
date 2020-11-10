package com.tokopedia.sellerorder.list.presentation.filtertabs

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
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

    private val context by lazy { sortFilter.context }

    private var selectedTab: SomListFilterUiModel.Status? = null
    private var filterItems: ArrayList<SortFilterItem> = arrayListOf()
    private var somListFilterUiModel: SomListFilterUiModel? = null
    private var selectedCount: Int = 0

    init {
        sortFilter.chipItems = arrayListOf()
        changeTabSortFilterText()
        selectParentFilter()
    }

    fun selectTabReset() {
        selectedTab = null
    }

    private fun changeTabSortFilterText() {
        sortFilter.textView.text = context.getString(com.tokopedia.sellerorder.R.string.som_list_filter_chip)
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
        changeTabSortFilterText()
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
        changeTabSortFilterText()
        updateCounter(selectedCount)
    }

    private fun scrollToTab(position: Int) {
        if (position < 0) return
        sortFilter.chipItems.getOrNull(position)?.let { tab ->
            val tabView = tab.refChipUnify
            val childOffset = Point()
            val scrollView = sortFilter.sortFilterHorizontalScrollView
            getDeepChildOffset(scrollView, tabView.parent, tabView, childOffset)
            scrollView.smoothScrollTo(childOffset.x, 0)
        }
    }

    private fun updateCounter(count: Int) {
        sortFilter.indicatorCounter = count + if (selectedTab != null) 1 else 0
    }

    /**
     * Used to get deep child offset.
     *
     *
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private fun getDeepChildOffset(mainParent: ViewGroup, parent: ViewParent, child: View, accumulatedOffset: Point) {
        val parentGroup = parent as ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == mainParent) {
            return
        }
        getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }

    fun updateCounterSortFilter(somListFilterUiModel: List<SomFilterUiModel>, filterDate: String) {
        var count = 0
        somListFilterUiModel.forEach {
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
        changeTabSortFilterText()
        selectedTab = status
        sortFilter.postDelayed({
            scrollToTab(filterItems.indexOfFirst { it.title.contains(status.status) })
            updateCounter(selectedCount)
        }, SWIPE_TAB_ANIMATION_DELAY)
    }

    fun updateCounterSortFilter() {
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

    fun shouldShowBulkAction() = selectedTab?.key == STATUS_NEW_ORDER
    fun isNewOrderFilterSelected(): Boolean = selectedTab?.key == STATUS_NEW_ORDER
    fun getSelectedFilterOrderCount(): Int = selectedTab?.amount.orZero()
    fun getSelectedFilterStatus(): String = selectedTab?.key.orEmpty()
    fun getSelectedFilterSatusName(): String = selectedTab?.status.orEmpty()

    fun getSelectedTab() = selectedTab

    fun getSomListFilterUiModel() = somListFilterUiModel

    interface SomListSortFilterTabClickListener {
        fun onParentSortFilterClicked()
        fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean)
    }
}