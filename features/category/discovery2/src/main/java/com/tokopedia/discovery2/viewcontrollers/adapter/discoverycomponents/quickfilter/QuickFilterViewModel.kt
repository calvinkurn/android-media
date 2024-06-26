package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.IQuickFilterGqlRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.QuickFilterUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val DEFAULT_SORT_ID = "23"
const val SORT_KEY = "ob"
const val SORT_FILTER_KEY = "sortfilter_ob"
class QuickFilterViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components), CoroutineScope {

    @JvmField
    @Inject
    var filterRepository: FilterRepository? = null

    @JvmField
    @Inject
    var quickFilterRepository: QuickFilterRepository? = null

    @JvmField
    @Inject
    var quickFilterGQLRepository: IQuickFilterGqlRepository? = null

    @JvmField
    @Inject
    var quickFilterUseCase: QuickFilterUseCase? = null
    private var selectedSort: HashMap<String, String> = HashMap()
    private val quickFilterList: ArrayList<Filter> = ArrayList()
    private val dynamicFilterModel: MutableLiveData<DynamicFilterModel> = MutableLiveData()
    private val quickFiltersLiveData: MutableLiveData<ArrayList<Filter>> = MutableLiveData()
    private val _filterCountLiveData: MutableLiveData<Int> = MutableLiveData()
    private val sortKeySet: MutableSet<String> = HashSet()
    private val productCountMutableLiveData = MutableLiveData<String?>()
    val productCountLiveData: LiveData<String?>
        get() = productCountMutableLiveData
    val filterCountLiveData: LiveData<Int>
        get() = _filterCountLiveData
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        fetchQuickFilters()
        addDefaultToSearchParameter()
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
    }

    fun fetchQuickFilters() {
        launchCatchError(block = {
            val filters = quickFilterRepository?.getQuickFilterData(components.id, components.pageEndPoint)
            addFilterOptions(components.data?.firstOrNull()?.filter ?: filters ?: arrayListOf())
        }, onError = {
                Timber.e(it)
            })
    }

    private fun addFilterOptions(filters: ArrayList<Filter>) {
        quickFilterList.clear()
        if (components.properties?.chipSize == Constant.ChipSize.LARGE) {
            addSelectedSortedFilterOptions(filters)
        } else {
            for (item in filters) {
                if (!item.options.isNullOrEmpty()) {
                    quickFilterList.add(item)
                }
            }
        }
        quickFiltersLiveData.value = quickFilterList
    }

    private fun addSelectedSortedFilterOptions(filters: ArrayList<Filter>) {
        val listSelected: ArrayList<Filter> = ArrayList()
        val listUnSelected: ArrayList<Filter> = ArrayList()
        for (item in filters) {
            if (!item.options.isNullOrEmpty()) {
                var optionSelected = false
                for (option in item.options) {
                    if (isQuickFilterSelected(option)) {
                        optionSelected = true
                        break
                    }
                }
                if (optionSelected) {
                    listSelected.add(item)
                } else {
                    listUnSelected.add(item)
                }
            }
        }
        quickFilterList.addAll(listSelected)
        quickFilterList.addAll(listUnSelected)
    }

    fun getTargetComponent(): ComponentsItem? {
        return Utils.getTargetComponentOfFilter(components)
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
                if (quickFilterUseCase?.onFilterApplied(component, selectedFilter, selectedSort) == true) {
                    syncData.value = true
                }
            }, onError = {
                    Timber.e(it)
                })
        }
    }

    fun fetchDynamicFilterModel() {
        launchCatchError(block = {
            var componentID = components.id
            if (components.properties?.dynamic == true && !components.dynamicOriginalId.isNullOrEmpty()) {
                componentID = components.dynamicOriginalId!!
            }
            val queryParameterMap = mutableMapOf<String, Any>()
            queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(components.userAddressData))
            dynamicFilterModel.value = filterRepository?.getFilterData(componentID, queryParameterMap, components.pageEndPoint)
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
            quickFiltersLiveData.value = quickFilterList
        }

        getSelectedFilterCount()
    }

    private fun clearDataFilterSort() {
        components.filters.clear()
        sortKeySet.clear()
    }

    private fun setFilterData(filters: List<Filter>?) {
        if (filters?.isNotEmpty() == true) {
            components.filters.addAll(filters)
            if (quickFilterList.isNotEmpty()) {
                components.filters.addAll(quickFilterList)
            }
        }
    }

    private fun setSortData(sort: List<Sort>?) {
        sort?.map {
            it.key
        }?.let {
            sortKeySet.addAll(it)
        }
    }

    fun isQuickFilterSelected(option: Option): Boolean {
        for (selectedFilter in components.filterController.filterViewStateSet) {
            if (option.uniqueId == selectedFilter) return true
        }
        return false
    }

    fun clearQuickFilters() {
        for (filter in quickFilterList)
            for (option in filter.options)
                components.filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = false)
        applyFilterToSearchParameter(components.filterController.getParameter())
        reloadData()
    }

    fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            if (option.inputType == Option.INPUT_TYPE_RADIO) {
                components.filterController.setFilter(option, isFilterApplied = true, isCleanUpExistingFilterWithSameKey = true)
            } else {
                components.filterController.setFilter(option, isFilterApplied = true, isCleanUpExistingFilterWithSameKey = false)
            }
        } else {
            components.filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = false)
        }
        applyFilterToSearchParameter(components.filterController.getParameter())
        setSelectedSort(components.filterController.getParameter())
        reloadData()
    }

    fun onDropDownFilterSelected(options: List<Option>) {
        for (option in options) {
            if (option.inputState.toBoolean()) {
                components.filterController.setFilter(
                    option,
                    isFilterApplied = true,
                    isCleanUpExistingFilterWithSameKey = false
                )
            } else {
                components.filterController.setFilter(
                    option,
                    isFilterApplied = false,
                    isCleanUpExistingFilterWithSameKey = false
                )
            }
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
        sortKeySet.forEach {
            if (mapParameter.containsKey(it)) {
                selectedSort[it] = mapParameter[it] ?: error("")
            }
        }
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
        val searchParameter = components.searchParameter

        if (searchParameter.contains(SORT_KEY) || searchParameter.contains(SORT_FILTER_KEY)) return

        val key = if (components.isFromCategory) {
            SORT_KEY
        } else {
            SORT_FILTER_KEY
        }

        searchParameter.set(key, DEFAULT_SORT_ID)
    }

    fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        setSelectedSort(applySortFilterModel.mapParameter)
        if (applySortFilterModel.selectedFilterMapParameter.isEmpty() && applySortFilterModel.selectedSortMapParameter.isEmpty()) {
            setSelectedFilter(HashMap(applySortFilterModel.selectedFilterMapParameter))
            applyFilterToSearchParameter(mapOf())
        } else {
            applyFilterToSearchParameter(applySortFilterModel.mapParameter)
            setSelectedFilter(HashMap(applySortFilterModel.mapParameter))
        }
        reloadData()
    }

    private fun getUserId(): String? {
        return UserSession(application).userId
    }

    fun getSelectedFilterCount() {
        val list = sortKeySet.filter {
            (components.searchParameter.contains(it) && components.searchParameter.get(it) != DEFAULT_SORT_ID && components.searchParameter.get(it).isNotEmpty())
        }
        _filterCountLiveData.value = if (list.isNullOrEmpty()) {
            components.filterController.getFilterCount()
        } else {
            components.filterController.getFilterCount() + 1
        }
    }

    fun filterProductsCount(selectedFilterMapParameter: Map<String, String>) {
        components.properties?.targetId?.let { targetID ->
            val targetList = targetID.split(",")
            if (targetList.isNotEmpty()) {
                launchCatchError(block = {
                    val formattedParameters = FilterKeyFormatter
                        .format(
                            selectedFilterMapParameter,
                            populateFilterKeys(components.filters)
                        )

                    productCountMutableLiveData.value = quickFilterGQLRepository
                        ?.getQuickFilterProductCountData(
                            targetList.first(),
                            components.pageEndPoint,
                            formattedParameters,
                            getUserId()
                        )?.component
                        ?.compAdditionalInfo
                        ?.totalProductData
                        ?.productCountWording.orEmpty()
                }, onError = { throwable ->
                        Timber.e(throwable)
                    })
            }
        }
    }

    private fun populateFilterKeys(filters: ArrayList<Filter>) = filters
        .map { it.options.first().key }
        .toSet()
}
