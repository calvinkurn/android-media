package com.tokopedia.product.manage.feature.list.view.ui.tab

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.feature.list.view.listener.ProductManageListListener
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
        const val ACTIVE_TAB = "Aktif"
        const val INACTIVE_TAB = "Nonaktif"
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
        updateTabs(tabs)
        setOnClickMoreFilter(tabs)
        changeTabSortFilterText()
    }

    fun update(data: GetFilterTabResult, productManageListListener: ProductManageListListener) {
        val tabs = data.tabs
        // keep index and prev filter of selected tab
        var selectedTabIndex = -1
        var prevfilter: ProductStatus? = null
        sortFilterTab.chipItems.forEachIndexed { i, chip ->
            if (chip.type == ChipsUnify.TYPE_SELECTED) {
                selectedTabIndex = i
                prevfilter = checkFilterContaining(chip.title)
                return@forEachIndexed
            }
        }

        // clear old items from sort filter tab
        sortFilterTab.chipItems.clear()
        sortFilterTab.sortFilterItems.removeAllViews()

        // add or remove the tabs
        updateTabs(tabs)
        val currentChipsCount = sortFilterTab.chipItems.count() - 1
        if(selectedTabIndex > currentChipsCount || selectedTabIndex < currentChipsCount) {
            // find the same chip has been selected before after removed
            sortFilterTab.chipItems.forEachIndexed { i, chip ->
                val newFilter = checkFilterContaining(chip.title)
                if (prevfilter == newFilter) {
                    selectedTabIndex = i
                    return@forEachIndexed
                }
            }
        }

        sortFilterTab.chipItems.forEachIndexed { i, chip ->
            if(i == selectedTabIndex) {
                // set initial counter with count of filter active
                sortFilterTab.indicatorCounter = activeFilterCount
                // select tab with prev index
                chip.type = ChipsUnify.TYPE_SELECTED
                selectedTab = SelectedTab(chip, data.tabs[selectedTabIndex].count)
                // get new filter and compare whether new filter still same or not with prev filter
                val newFilter = checkFilterContaining(chip.title)
                if (prevfilter != newFilter) {
                    // get product list again cause product list will be different
                    productManageListListener.clearAndGetProductList()
                }
                return@forEachIndexed
            }
        }
        changeTabSortFilterText()

    }

    fun getSelectedFilter(): ProductStatus? {
        val selectedFilter = selectedTab?.filter
        return checkFilterContaining(selectedFilter?.title)
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

    private fun checkFilterContaining(title: CharSequence?): ProductStatus? {
        title?.let {
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
