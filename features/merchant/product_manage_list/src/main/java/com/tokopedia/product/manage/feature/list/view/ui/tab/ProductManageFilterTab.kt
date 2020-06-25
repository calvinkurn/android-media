package com.tokopedia.product.manage.feature.list.view.ui.tab

import com.tokopedia.kotlin.extensions.view.orZero
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
        const val ACTIVE_TAB = "Aktif"
        const val INACTIVE_TAB = "Nonaktif"
    }

    private var activeFilterCount = 0
    private var selectedTab: SelectedTab? = null

    private val context by lazy { sortFilterTab.context }

    init {
        sortFilterTab.chipItems = arrayListOf()
    }

    fun show(data: GetFilterTabResult) {
        val tabs = data.tabs
        updateTabs(tabs)
        setOnClickMoreFilter(tabs)
    }

    fun update(data: GetFilterTabResult) {
        val tabs = data.tabs
        // keep index of selected tab
        var selectedTabIndex = -1
        sortFilterTab.chipItems.forEachIndexed { i, chip ->
            if (chip.type == ChipsUnify.TYPE_SELECTED) {
                selectedTabIndex = i
                return@forEachIndexed
            }
        }
        // clear old items from sort filter tab
        sortFilterTab.chipItems.clear()
        sortFilterTab.sortFilterItems.removeAllViews()
        // add or remove the tabs
        updateTabs(tabs)
        // select tab with prev index
        sortFilterTab.chipItems.forEachIndexed { i, chip ->
            if(i == selectedTabIndex) {
                // set initial counter with count of filter active
                sortFilterTab.indicatorCounter = activeFilterCount
                chip.type = ChipsUnify.TYPE_SELECTED
                selectedTab = SelectedTab(chip, data.tabs[selectedTabIndex].count)
                return@forEachIndexed
            }
        }
    }

    private fun updateTabs(tabs: List<FilterTabViewModel>) {
        sortFilterTab.apply {
            val filterTabs = tabs.map { tab ->
                val title = context.getString(tab.titleId, tab.count)

                val filter = SortFilterItem(title)
                filter.listener = { toggleFilterTab(filter, tab) }

                filter
            }
            addItem(ArrayList(filterTabs))
        }
    }

    fun getSelectedFilter(): ProductStatus? {
        val selectedFilter = selectedTab?.filter
        selectedFilter?.title?.let {
            return when {
                it.contains(ACTIVE_TAB) -> {
                    ProductStatus.ACTIVE
                }
                it.contains(INACTIVE_TAB) -> {
                    ProductStatus.INACTIVE
                }
                else -> {
                    ProductStatus.VIOLATION
                }
            }
        }
        return null
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
}
