package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.*
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class QuickFilterViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), SortFilterBottomSheet.Callback {
    private var initializedFilterList: List<Filter>? = null
    private lateinit var quickFilterViewModel: QuickFilterViewModel
    private val quickSortFilter: SortFilter = itemView.findViewById(R.id.quick_sort_filter)
    private var quickFilterOptionList: java.util.ArrayList<Option> = ArrayList()
    private var sortFilterBottomSheet: SortFilterBottomSheet = SortFilterBottomSheet()
    private var searchParameter: SearchParameter = SearchParameter()
    private var dynamicFilterModel: DynamicFilterModel? = null
    private val filterController: FilterController = FilterController()
    private var filters: ArrayList<Filter> = ArrayList()
    private var sort: ArrayList<Sort> = ArrayList()
    private var selectedSort: HashMap<String, String> = HashMap()
    private var filterTrackingData: FilterTrackingData? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        quickFilterViewModel = discoveryBaseViewModel as QuickFilterViewModel
        setObserver()
        if (getSelectedFilter().isNullOrEmpty()) {
            setQuickFilters()
            quickFilterViewModel.fetchDynamicFilterModel()
        }
    }

    private fun setObserver() {
        quickFilterViewModel.getDynamicFilterModelLiveData().observe(fragment.viewLifecycleOwner, Observer {
            if (it != null) {
                dynamicFilterModel = it
                renderDynamicFilter(it.data)
            }
        })
        quickFilterViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            if (item) {
                (fragment as DiscoveryFragment).reSync()
            }
        })
    }

    private fun renderDynamicFilter(data: DataValue) {
        clearDataFilterSort()
        setFilterData(data.filter)
        setSortData(data.sort)

        initializedFilterList = FilterHelper.initializeFilterList(filters)
        filterController.initFilterController(searchParameter.getSearchParameterHashMap(), initializedFilterList)
    }

    private fun clearDataFilterSort() {
        this.filters.clear()
        this.sort.clear()
    }

    private fun setFilterData(filters: List<Filter>) {
        this.filters = ArrayList()
        this.filters.addAll(filters)
    }

    private fun setSortData(sorts: List<Sort>) {
        this.sort = ArrayList()
        this.sort.addAll(sorts)
    }

    private fun setQuickFilters() {
        quickSortFilter.textView.text = fragment.getString(R.string.filter)
        quickSortFilter.parentListener = { openBottomSheetFilterRevamp() }
        quickSortFilter.sortFilterItems.removeAllViews()
        val sortFilterItems: ArrayList<SortFilterItem> = ArrayList()
        if (quickFilterViewModel.components.data?.isEmpty() == true) return
        val quickFilters = quickFilterViewModel.components.data?.get(0)?.filter ?: ArrayList()

        for (item in quickFilters) {
            quickFilterOptionList.addAll(item.options)
            for (option in item.options) {
                sortFilterItems.add(createSortFilterItem(option))
            }
        }
        quickSortFilter.addItem(sortFilterItems)
    }

    private fun createSortFilterItem(option: Option): SortFilterItem {
        val item = SortFilterItem(option.name)
        item.listener = {
            onQuickFilterSelected(option)
        }
        if (isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
        return item
    }

    private fun refreshQuickFilter() {
        val options: List<Option> = quickFilterOptionList
        setSortFilterItemState(options)
    }

    private fun setSortFilterItemState(options: List<Option>) {
        if (quickFilterOptionList.size != quickSortFilter.chipItems.size) return
        for (i in options.indices) {
            if (isQuickFilterSelected(options[i])) setQuickFilterChipsSelected(i) else setQuickFilterChipsNormal(i)
        }
    }

    private fun setQuickFilterChipsSelected(position: Int) {
        quickSortFilter.chipItems[position].type = ChipsUnify.TYPE_SELECTED
        quickSortFilter.chipItems[position].refChipUnify.chipType = ChipsUnify.TYPE_SELECTED
    }

    private fun setQuickFilterChipsNormal(position: Int) {
        quickSortFilter.chipItems[position].type = ChipsUnify.TYPE_NORMAL
        quickSortFilter.chipItems[position].refChipUnify.chipType = ChipsUnify.TYPE_NORMAL
    }

    private fun isQuickFilterSelected(option: Option): Boolean {
        return getSelectedFilter().containsKey(option.key)
    }

    private fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
//            eventQuickFilterClicked(mDepartmentId, option, true)
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
//            eventQuickFilterClicked(mDepartmentId, option, false)
        }
    }

    private fun getSelectedFilter(): HashMap<String, String> {
        return HashMap(filterController.getActiveFilterMap())
    }

    private fun openBottomSheetFilterRevamp() {
//        eventOpenFilterPage(getFilterTrackingData())
        sortFilterBottomSheet.show(
                fragment.childFragmentManager,
                searchParameter.getSearchParameterHashMap(),
                dynamicFilterModel,
                this
        )
    }

    private fun applyFilterToSearchParameter(filterParameter: Map<String, String>) {
        this.searchParameter.getSearchParameterHashMap().clear()
        this.searchParameter.getSearchParameterHashMap().putAll(filterParameter)
    }

    private fun setSelectedFilter(selectedFilter: HashMap<String, String>) {
        if (filters.isEmpty()) return
        filterController.initFilterController(selectedFilter, initializedFilterList)
    }

    private fun reloadData() {
        refreshQuickFilter()
        quickFilterViewModel.onFilterApplied(getSelectedFilter(), selectedSort)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        selectedSort.putAll(applySortFilterModel.selectedSortMapParameter)
        applyFilterToSearchParameter(applySortFilterModel.mapParameter)
        setSelectedFilter(HashMap(applySortFilterModel.mapParameter))
        reloadData()
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        sortFilterBottomSheet.setResultCountText(fragment.getString(R.string.bottom_sheet_filter_finish_button_text))
    }

    /**comments for tracking in future**/
//    private fun getFilterTrackingData(): FilterTrackingData? {
//        if (filterTrackingData == null) {
//            filterTrackingData = FilterTrackingData(
//                    FilterEventTracking.Event.CLICK_SEARCH_RESULT,
//                    FilterEventTracking.Category.FILTER_PRODUCT,
//                    "",
//                    FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
//            )
//        }
//        return filterTrackingData
//    }

}

