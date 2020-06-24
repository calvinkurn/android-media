package com.tokopedia.product.manage.feature.list.view.ui.tab

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class ProductManageFilterTab(
        private val sortFilterTab: SortFilter,
        private val onClickMoreFilter: (FilterTabViewModel) -> Unit,
        private val onClickFilterTab: (FilterTabViewModel) -> Unit
) {

    companion object {
        const val ACTIVE_TAB_POSITION = 0
        const val INACTIVE_TAB_POSITION = 1
        const val VIOLATION_TAB_POSITION = 2
    }

    private var activeFilterCount = 0
    private var selectedTab: SelectedTab? = null

    private val context by lazy { sortFilterTab.context }

    init {
        sortFilterTab.chipItems = arrayListOf()
        changeTabSortFilterText()
    }

    fun show(data: GetFilterTabResult) {
        val tabs = data.tabs
        sortFilterTab.apply {
            val filterTabs = tabs.map { tab ->
                val title = context.getString(tab.titleId, tab.count)

                val filter = SortFilterItem(title)
                filter.listener = { toggleFilterTab(filter, tab) }

                filter
            }
            addItem(ArrayList(filterTabs))
        }

        changeTabSortFilterText()
        setOnClickMoreFilter(tabs)
    }

    fun update(data: GetFilterTabResult) {
        val tabs = data.tabs

        tabs.forEachIndexed { index, tab ->
            val filter = sortFilterTab.chipItems.getOrNull(index)
            if(filter != null) {
                val chipText = context.getString(tab.titleId, tab.count)
                val chipType = filter.type
                sortFilterTab.chipItems[index] = filter.apply {
                    title = chipText
                    listener = { toggleFilterTab(filter, tab) }
                }
                if(chipType == ChipsUnify.TYPE_SELECTED) {
                    setSelectedFilter(filter, tab)
                }
            } else {
                val title = context.getString(tab.titleId, tab.count)
                val item = SortFilterItem(title)
                sortFilterTab.chipItems.add(item)
            }
        }
        changeTabSortFilterText()
    }

    fun getSelectedFilter(): ProductStatus? {
        val chipItems = sortFilterTab.chipItems
        val selectedFilter = selectedTab?.filter
        val index = chipItems.indexOf(selectedFilter)

        return when (index) {
            ACTIVE_TAB_POSITION -> ProductStatus.ACTIVE
            INACTIVE_TAB_POSITION -> ProductStatus.INACTIVE
            VIOLATION_TAB_POSITION -> ProductStatus.VIOLATION
            else -> null
        }
    }

    fun setActiveFilterCount(count: Int) {
        activeFilterCount = count

        sortFilterTab.indicatorCounter = if (isFilterActive()) {
            val selectedTabCount = sortFilterTab.chipItems
                    .filter { it.type == ChipsUnify.TYPE_SELECTED }
                    .count()

            activeFilterCount + selectedTabCount
        } else {
            activeFilterCount
        }
        changeTabSortFilterText()
    }

    fun getProductCount(): Int = selectedTab?.count.orZero()

    fun isFilterActive() = selectedTab != null

    fun resetFilters() {
        resetSelectedFilter()
        setActiveFilterCount(0)
    }

    private fun toggleFilterTab(filter: SortFilterItem, tab: FilterTabViewModel) {
        val selectedFilter = selectedTab?.filter

        if (filter == selectedFilter) {
            if (filter.type == ChipsUnify.TYPE_NORMAL) {
                filter.type = ChipsUnify.TYPE_SELECTED
                setSelectedFilter(filter, tab)
                setActiveFilterCount(activeFilterCount)
            } else {
                resetSelectedFilter()
                filter.type = ChipsUnify.TYPE_NORMAL
                setActiveFilterCount(activeFilterCount)
            }
        } else {
            resetSelectedFilter()
            filter.type = ChipsUnify.TYPE_SELECTED
            setSelectedFilter(filter, tab)
        }

        onClickFilterTab(tab)
    }

    private fun setOnClickMoreFilter(tabs: List<FilterTabViewModel>) {
        tabs.firstOrNull()?.let { clickMoreFilterTab(it) }
    }

    private fun clickMoreFilterTab(tab: FilterTabViewModel) {
        val moreFilterTab = sortFilterTab.sortFilterPrefix
        moreFilterTab.setOnClickListener {
            onClickMoreFilter(tab)
        }
    }

    private fun setSelectedFilter(filter: SortFilterItem, tab: FilterTabViewModel) {
        selectedTab = SelectedTab(filter, tab.count)
    }

    private fun resetSelectedFilter() {
        selectedTab?.filter?.apply {
            type = ChipsUnify.TYPE_NORMAL
        }
        selectedTab = null
    }

    private fun changeTabSortFilterText() {
        sortFilterTab.textView.text = context.getString(R.string.product_manage_filter)
    }
}
