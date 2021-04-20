package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.IQuickFilterGqlRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.QuickFilterUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.*
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val RPC_FILTER_KEY = "rpc_"
const val DEFAULT_SORT_ID = "23"
const val SORT_KEY = "ob"
class QuickFilterViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var filterRepository: FilterRepository

    @Inject
    lateinit var quickFilterRepository: QuickFilterRepository
    @Inject
    lateinit var quickFilterGQLRepository: IQuickFilterGqlRepository
    @Inject
    lateinit var quickFilterUseCase: QuickFilterUseCase
    private var selectedSort: HashMap<String, String> = HashMap()
    private var sort: ArrayList<Sort> = ArrayList()
    private val quickFilterOptionList: ArrayList<Option> = ArrayList()
    private val dynamicFilterModel: MutableLiveData<DynamicFilterModel> = MutableLiveData()
    private val quickFiltersLiveData: MutableLiveData<ArrayList<Option>> = MutableLiveData()

    private val productCountMutableLiveData = MutableLiveData<String?>()
    val productCountLiveData: LiveData<String?>
        get() = productCountMutableLiveData

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        fetchQuickFilters()
        addDefaultToSearchParameter()
    }

    fun fetchQuickFilters() {
        launchCatchError(block = {
            val filters = quickFilterRepository.getQuickFilterData(components.id, components.pageEndPoint)
            addFilterOptions(components.data?.firstOrNull()?.filter ?: filters ?: arrayListOf())
        }, onError = {
            Timber.e(it)
        })
    }

    private fun addFilterOptions(filters: ArrayList<Filter>) {
        quickFilterOptionList.clear()
        for (item in filters) {
            if (!item.options.isNullOrEmpty()) {
                quickFilterOptionList.addAll(item.options)
            }
        }
        quickFiltersLiveData.value = quickFilterOptionList
    }

    fun getTargetComponent(): ComponentsItem? {
        return getComponent(components.properties?.targetId ?: "", components.pageEndPoint)
    }

    fun getDynamicFilterModelLiveData() = dynamicFilterModel
    fun getQuickFilterLiveData() = quickFiltersLiveData

    private fun onFilterApplied(selectedFilter: HashMap<String, String>?, selectedSort: HashMap<String, String>?) {
        getTargetComponent()?.let { component ->
            components.selectedFilters = selectedFilter
            components.selectedSort = selectedSort
            component.selectedFilters = selectedFilter
            component.selectedSort = selectedSort
            launchCatchError(block = {
                if (quickFilterUseCase.onFilterApplied(component, selectedFilter, selectedSort)) {
                    syncData.value = true
                }
            }, onError = {
                Timber.e(it)
            })
        }
    }

    fun fetchDynamicFilterModel() {
        launchCatchError(block = {
            dynamicFilterModel.value = filterRepository.getFilterData(components.id, mutableMapOf(), components.pageEndPoint)
            renderDynamicFilter(dynamicFilterModel.value?.data)
        }, onError = {
            Timber.e(it)
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

    fun clearQuickFilters(){
        for (option in quickFilterOptionList)
            components.filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = false)
        applyFilterToSearchParameter(components.filterController.getParameter())
        reloadData()
    }

    fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            components.filterController.setFilter(option, isFilterApplied = true, isCleanUpExistingFilterWithSameKey = false)
        } else {
            components.filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = false)
        }
        applyFilterToSearchParameter(components.filterController.getParameter())
        setSelectedSort(components.filterController.getParameter())
        reloadData()
    }

    private fun applyFilterToSearchParameter(filterParameter: Map<String, String>) {
        components.searchParameter.getSearchParameterHashMap().clear()
        components.searchParameter.getSearchParameterHashMap().putAll(filterParameter)
    }

    private fun setSelectedSort(mapParameter: Map<String, String>) {
        if(mapParameter.containsKey(SORT_KEY))
            selectedSort[SORT_KEY] = mapParameter[SORT_KEY] ?: error("")
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

    private fun addDefaultToSearchParameter() {
        if(!components.searchParameter.contains(SORT_KEY))
            components.searchParameter.set(SORT_KEY, DEFAULT_SORT_ID)
    }

    fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        setSelectedSort(applySortFilterModel.mapParameter)
        applyFilterToSearchParameter(applySortFilterModel.mapParameter)
        setSelectedFilter(HashMap(applySortFilterModel.mapParameter))
        reloadData()
    }

    private fun getUserId(): String? {
        return UserSession(application).userId
    }

    fun getSelectedFilterCount() : Int {
        return if(!components.searchParameter.contains(SORT_KEY) || components.searchParameter.get(SORT_KEY) == DEFAULT_SORT_ID) {
            components.filterController.filterViewStateSet.size
        } else {
            components.filterController.filterViewStateSet.size + 1
        }
    }

    fun filterProductsCount(selectedFilterMapParameter: Map<String, String>) {
        components.properties?.targetId?.let { targetID ->
            val targetList = targetID.split(",")
            if (targetList.isNotEmpty()) {
                launchCatchError(block = {
                    productCountMutableLiveData.value = quickFilterGQLRepository
                            .getQuickFilterProductCountData(targetList.first(),
                                    components.pageEndPoint, appendRPCInKey(selectedFilterMapParameter),
                                    getUserId()).component?.compAdditionalInfo?.totalProductData
                            ?.productCountWording ?: ""
                }, onError = {
                    it.printStackTrace()
                })
            }
        }
    }

    private fun appendRPCInKey(selectedFilterMapParameter: Map<String, String>): MutableMap<String, String> {
        val filtersQueryParameterMap = mutableMapOf<String, String>()
        selectedFilterMapParameter.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                filtersQueryParameterMap[RPC_FILTER_KEY + key] = value
            }
        }
        return filtersQueryParameterMap
    }
}
