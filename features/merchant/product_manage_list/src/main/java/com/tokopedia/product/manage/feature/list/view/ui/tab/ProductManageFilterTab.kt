package com.tokopedia.product.manage.feature.list.view.ui.tab

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.feature.list.view.listener.ProductManageListListener
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class ProductManageFilterTab(
    private val sortFilterTab: SortFilter,
    private val onClickMoreFilter: () -> Unit,
    private val onClickFilterTab: (FilterTabUiModel) -> Unit
) {

    private var activeFilterCount = 0
    private var selectedTab: SelectedTab? = null

    private val context by lazy { sortFilterTab.context }

    init {
        sortFilterTab.chipItems = arrayListOf()
        changeTabSortFilterText()
    }

    fun show(data: GetFilterTabResult) {
        val tabs = data.tabs
        updateTabs(tabs)
        setOnClickMoreFilter()
        changeTabSortFilterText()
    }

    fun update(data: GetFilterTabResult, productManageListListener: ProductManageListListener) {
        val tabs = data.tabs
        // keep index and prev filter of selected tab
        var selectedTabIndex = -1
        sortFilterTab.chipItems?.forEachIndexed { i, chip ->
            if (chip.type == ChipsUnify.TYPE_SELECTED) {
                selectedTabIndex = i
                return@forEachIndexed
            }
        }
        // clear old items from sort filter tab
        sortFilterTab.sortFilterItems.removeAllViews()

        // add or remove the tabs
        updateTabs(tabs)
        val currentChipsCount = sortFilterTab.chipItems?.count() ?: 0 - 1
        if(selectedTabIndex > currentChipsCount) {
            //if selectedTab more than current chips
            // set chip to be the last of chips
            selectedTabIndex = currentChipsCount
        }

        sortFilterTab.chipItems?.forEachIndexed { i, chip ->
            if(i == selectedTabIndex) {
                // set initial counter with count of filter active
                sortFilterTab.indicatorCounter = activeFilterCount
                // select tab with prev index
                chip.type = ChipsUnify.TYPE_SELECTED

                val prevStatusName = selectedTab?.status
                val tabSelectedBasedIndex = tabs[selectedTabIndex]
                tabSelectedBasedIndex.status?.let {
                    selectedTab = SelectedTab(chip, tabSelectedBasedIndex.count, it)
                }
                val newStatusName = selectedTab?.status

                // check whether the status name still the same or not
                if (prevStatusName != newStatusName) {
                    // get product list again cause product list will be different
                    productManageListListener.clearAndGetProductList()
                }
                return@forEachIndexed
            }
        }
        changeTabSortFilterText()
        setOnClickMoreFilter()
    }

    fun getSelectedFilter(): ProductStatus? {
        return selectedTab?.status
    }

    fun setActiveFilterCount(count: Int) {
        activeFilterCount = count

        sortFilterTab.indicatorCounter = if (isFilterActive()) {
            val selectedTabCount = sortFilterTab.chipItems
                    ?.filter { it.type == ChipsUnify.TYPE_SELECTED }
                    ?.count() ?: 0

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

    private fun updateTabs(tabs: List<FilterTabUiModel>) {
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

    private fun toggleFilterTab(filter: SortFilterItem, tab: FilterTabUiModel) {
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

    private fun setOnClickMoreFilter() {
        sortFilterTab.sortFilterPrefix.setOnClickListener {
            onClickMoreFilter()
        }
    }

    private fun setSelectedFilter(filter: SortFilterItem, tab: FilterTabUiModel) {
        tab.status?.let { selectedTab = SelectedTab(filter, tab.count, it) }
    }

    private fun resetSelectedFilter() {
        selectedTab?.filter?.apply {
            type = ChipsUnify.TYPE_NORMAL
        }
        selectedTab = null
    }

    private fun changeTabSortFilterText() {
        sortFilterTab.textView?.text = context.getString(R.string.product_manage_filter)
    }
}
