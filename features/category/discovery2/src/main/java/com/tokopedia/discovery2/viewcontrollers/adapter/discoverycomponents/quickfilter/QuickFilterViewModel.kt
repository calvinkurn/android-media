package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.QuickFilterUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.*
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.isNotEmpty
import kotlin.collections.isNullOrEmpty
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext

class QuickFilterViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var quickFilterRepository: QuickFilterRepository

    @Inject
    lateinit var quickFilterUseCase: QuickFilterUseCase

    private var selectedSort: HashMap<String, String> = HashMap()
    private var sort: ArrayList<Sort> = ArrayList()
    private val quickFilterOptionList: ArrayList<Option> = ArrayList()

    private val dynamicFilterModel: MutableLiveData<DynamicFilterModel> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
        for (item in components.data?.get(0)?.filter ?: ArrayList()) {
            if (!item.options.isNullOrEmpty()) {
                quickFilterOptionList.addAll(item.options)
            }
        }
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getTargetComponent(): ComponentsItem? {
        return getComponent(components.properties?.targetId ?: "", components.pageEndPoint)
    }

    fun getQuickFiltersList(): ArrayList<Option> {
        return quickFilterOptionList
    }

    fun getDynamicFilterModelLiveData() = dynamicFilterModel

    private fun onFilterApplied(selectedFilter: HashMap<String, String>?, selectedSort: HashMap<String, String>?) {
        getTargetComponent()?.let { component ->
            component.selectedFilters = selectedFilter
            component.selectedSort = selectedSort
            launchCatchError(block = {
                if (quickFilterUseCase.onFilterApplied(component, selectedFilter, selectedSort)) {
                    syncData.value = true
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    fun fetchDynamicFilterModel() {
        launchCatchError(block = {
            dynamicFilterModel.value = quickFilterRepository.getQuickFilterData(components.id, mutableMapOf(), components.pageEndPoint)
            renderDynamicFilter(dynamicFilterModel.value?.data)
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun renderDynamicFilter(data: DataValue?) {
        clearDataFilterSort()
        setFilterData(data?.filter)
        setSortData(data?.sort)

        if (components.filterController.filterViewStateSet.isNullOrEmpty()) {
            val initializedFilterList = FilterHelper.initializeFilterList(components.filters)
            components.filterController.initFilterController(components.searchParameter.getSearchParameterHashMap(), initializedFilterList)
        }
    }

    private fun clearDataFilterSort() {
        components.filters.clear()
        this.sort.clear()
    }

    private fun setFilterData(filters: List<Filter>?) {
        if (filters?.isNotEmpty() == true)
            components.filters.addAll(filters)
    }

    private fun setSortData(sort: List<Sort>?) {
        this.sort = ArrayList()
        if (sort?.isNotEmpty() == true)
            this.sort.addAll(sort)
    }

    fun isQuickFilterSelected(option: Option): Boolean {
        for (selectedFilter in components.filterController.filterViewStateSet) {
            if (option.uniqueId == selectedFilter) return true
        }
        return false
    }

    fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            components.filterController.setFilter(option, isFilterApplied = true, isCleanUpExistingFilterWithSameKey = false)
        } else {
            components.filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = false)
        }
        applyFilterToSearchParameter(components.filterController.getParameter())
        reloadData()
    }

    private fun applyFilterToSearchParameter(filterParameter: Map<String, String>) {
        components.searchParameter.getSearchParameterHashMap().clear()
        components.searchParameter.getSearchParameterHashMap().putAll(filterParameter)
    }

    private fun refreshFilterController(queryParams: HashMap<String, String>) {
        val params = HashMap(queryParams)
        params[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        val initializedFilterList = FilterHelper.initializeFilterList(components.filters)
        components.filterController.initFilterController(params, initializedFilterList)
    }

    private fun setSelectedSort(selectedSortMapParameter: Map<String, String>) {
        selectedSort.putAll(selectedSortMapParameter)
    }

    private fun setSelectedFilter(selectedFilter: HashMap<String, String>) {
        if (components.filters.isEmpty()) return
        val initializedFilterList = FilterHelper.initializeFilterList(components.filters)
        components.filterController.initFilterController(selectedFilter, initializedFilterList)
    }

    private fun reloadData() {
        onFilterApplied(getSelectedFilter(), selectedSort)
    }

    private fun getSelectedFilter(): HashMap<String, String> {
        return HashMap(components.filterController.getActiveFilterMap())
    }

    fun getSearchParameterHashMap() = components.searchParameter.getSearchParameterHashMap()

    fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        setSelectedSort(applySortFilterModel.selectedSortMapParameter)
        applyFilterToSearchParameter(applySortFilterModel.mapParameter)
        setSelectedFilter(HashMap(applySortFilterModel.mapParameter))
        reloadData()
    }
}