package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodSearchMerchantUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiEvent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiState
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokofoodSearchResultPageViewModel @Inject constructor(
    private val tokofoodSearchMerchantUseCase: TokofoodSearchMerchantUseCase,
    private val tokofoodFilterSortUseCase: TokofoodFilterSortUseCase,
    private val tokofoodMerchantSearchResultMapper: TokofoodMerchantSearchResultMapper,
    private val tokofoodFilterSortMapper: TokofoodFilterSortMapper,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _searchKeyword = MutableSharedFlow<String>(Int.ONE)
    private val _searchMap = MutableSharedFlow<HashMap<String, String>>(Int.ONE)
    private val _uiState = MutableSharedFlow<TokofoodSearchUiState>(Int.ONE)

    @FlowPreview
    private val _sortFilterUiModel = MutableSharedFlow<Result<List<TokofoodSortFilterItemUiModel>>>(Int.ONE)
    val sortFilterUiModel: SharedFlow<Result<List<TokofoodSortFilterItemUiModel>>>
        get() = _sortFilterUiModel

    private val _uiEventFlow = MutableSharedFlow<TokofoodSearchUiEvent>(Int.ONE)
    val uiEventFlow: SharedFlow<TokofoodSearchUiEvent>
        get() = _uiEventFlow

    private val _appliedFilterCount = MutableSharedFlow<Int>(Int.ONE)
    val appliedFilterCount: SharedFlow<Int>
        get() = _appliedFilterCount

    val searchParameters: SharedFlow<SearchParameter?> =
        combine(_searchKeyword, _searchMap) { keyword, map ->
            val searchParameter = currentSearchParameter.value ?: SearchParameter(ApplinkConstInternalTokoFood.SEARCH)
            map.entries.forEach {
                searchParameter.set(it.key, it.value)
            }
            if (keyword.length >= MIN_SEARCH_KEYWORD_LENGTH) {
                searchParameter.set(SearchApiConst.Q, keyword)
            }
            searchParameter
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARING_DELAY_MILLIS),
            replay = Int.ONE
        )

    @FlowPreview
    val visitables: SharedFlow<Result<List<Visitable<*>>>> =
        _uiState.flatMapConcat { uiState ->
            getVisitablesFlow(uiState)
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARING_DELAY_MILLIS),
            replay = Int.ONE
        )

    private val localCacheModelLiveData = MutableLiveData<LocalCacheModel>()
    private val pageKeyLiveData = MutableLiveData<String>()
    private val currentVisitables = MutableLiveData<List<Visitable<*>>>()
    private val currentSearchParameter = MutableLiveData<SearchParameter>()
    private val dynamicFilterModel = MutableLiveData<DynamicFilterModel>()
    private val currentSortFilterUiModels = MutableLiveData<List<TokofoodSortFilterItemUiModel>>()

    init {
        _searchMap.tryEmit(hashMapOf())
    }

    fun setKeyword(keyword: String) {
        _searchKeyword.tryEmit(keyword)
    }

    fun setLocalCacheModel(localCacheModel: LocalCacheModel) {
        localCacheModelLiveData.value = localCacheModel
    }

    fun loadQuickSortFilter(searchParameter: SearchParameter) {
        if (shouldLoadSortFilter(searchParameter)) {
            launchCatchError(
                block = {
                    val uiModels = currentSortFilterUiModels.value.let { currentUiModels ->
                        if (currentUiModels.isNullOrEmpty()) {
                            val dataValue = withContext(dispatcher.io) {
                                tokofoodFilterSortUseCase.execute(TokofoodFilterSortUseCase.TYPE_QUICK)
                            }
                            tokofoodFilterSortMapper.getQuickSortFilterUiModels(dataValue)
                        } else {
                            tokofoodFilterSortMapper.getAppliedSortFilterUiModels(searchParameter, currentUiModels)
                        }
                    }
                    currentSortFilterUiModels.value = uiModels
                    setIndicatorCount()
                    _sortFilterUiModel.emit(Success(uiModels))
                },
                onError = {
                    _sortFilterUiModel.emit(Fail(it))
                }
            )
        }
    }

    fun resetFilterSearch() {
        currentSearchParameter.value = null
        _searchMap.tryEmit(hashMapOf())
    }

    fun getInitialMerchantSearchResult(searchParameter: SearchParameter?) {
        pageKeyLiveData.value = null
        currentSearchParameter.value = searchParameter
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_LOAD_INITIAL
            )
        )
        launchCatchError(
            block = {
                val searchResult = getSearchResult()
                _uiState.emit(getInitialSearchResultSuccessState(searchResult))
            },
            onError = {
                _uiState.emit(
                    TokofoodSearchUiState(
                        state = TokofoodSearchUiState.STATE_ERROR_INITIAL,
                        throwable = it
                    )
                )
            }
        )
    }

    fun onScrollProductList(lastVisibleItemIndex: Int, itemCount: Int) {
        if (shouldLoadMore(lastVisibleItemIndex, itemCount)) {
            _uiState.tryEmit(
                TokofoodSearchUiState(
                    state = TokofoodSearchUiState.STATE_LOAD_MORE
                )
            )
            loadMoreSearch()
        }
    }

    fun openDetailFilterBottomSheet() {
        launchCatchError(
            block = {
                _uiEventFlow.emit(
                    TokofoodSearchUiEvent(
                        state = TokofoodSearchUiEvent.EVENT_OPEN_DETAIL_BOTTOMSHEET,
                        data = dynamicFilterModel.value
                    )
                )
            },
            onError = {}
        )
        // TODO: Check whether we need to always load detail filter
        if (dynamicFilterModel.value == null) {
            loadDetailSortFilter()
        }
    }

    fun applySort(sort: Sort, isSelected: Boolean) {
        val value =
            if (isSelected) {
                sort.value
            } else {
                String.EMPTY
            }
        currentSearchParameter.value?.run {
            set(sort.key, value)
            _searchMap.tryEmit(getSearchParameterHashMap())
        }
    }

    fun applySortSelected(uiModel: TokofoodQuickSortUiModel) {
        currentSearchParameter.value?.run {
            set(uiModel.key, uiModel.value)
            _searchMap.tryEmit(getSearchParameterHashMap())
        }
    }

    fun applyFilter(filter: Filter) {
        currentSearchParameter.value?.run {
            getAppliedFilterMap(filter)?.let { (key, value) ->
                set(key, value)
            }

            _searchMap.tryEmit(getSearchParameterHashMap())
        }
    }

    fun applyOptions(options: List<Option>) {
        currentSearchParameter.value?.run {
            getAppliedFilterMap(options).entries.forEach {
                set(it.key, it.value)
            }

            _searchMap.tryEmit(getSearchParameterHashMap())
        }
    }

    fun resetParams(queryParams: Map<String, String>) {
        currentSearchParameter.value?.run {
            resetParams(queryParams)
            _searchMap.tryEmit(getSearchParameterHashMap())
        }
    }

    fun showQuickSortBottomSheet(sort: List<Sort>) {
        sort.firstOrNull()?.key?.let { currentKey ->
            currentSearchParameter.value?.get(currentKey)?.let { selectedSortValue ->
                showQuickSortBottomSheet(sort, selectedSortValue)
            }
        }
    }

    fun showQuickFilterBottomSheet(filter: Filter) {
        val uiEvent = getQuickFilterBottomSheetUiEvent(filter)
        _uiEventFlow.tryEmit(uiEvent)
    }

    private fun setIndicatorCount() {
        currentSearchParameter.value?.let { searchParameter ->
            _appliedFilterCount.tryEmit(searchParameter.getActiveCount())
        }
    }

    private fun SearchParameter.resetParams(queryParams: Map<String, String>) {
        getSearchParameterHashMap().clear()
        getSearchParameterHashMap().putAll(queryParams)
    }

    private fun SearchParameter.getActiveCount(): Int {
        val searchMap = getSearchParameterHashMap().filter { it.value.isNotEmpty() }
        return getSortFilterCount(searchMap)
    }

    private fun shouldLoadSortFilter(searchParameter: SearchParameter): Boolean {
        return !getIsSortFilterLoaded() || isSearchParameterNew(searchParameter)
    }

    private fun getIsSortFilterLoaded(): Boolean {
        return !currentSortFilterUiModels.value.isNullOrEmpty()
    }

    private fun isSearchParameterNew(searchParameter: SearchParameter): Boolean {
        return currentSearchParameter.value?.getSearchParameterMap() == searchParameter.getSearchParameterMap()
    }

    private fun getAppliedFilterMap(filter: Filter): Pair<String, String>? {
        val key = filter.options.firstOrNull()?.key ?: return null
        val value = filter.options.filter { it.inputState.toBoolean() }
            .joinToString(TokofoodFilterSortMapper.OPTION_SEPARATOR) { it.value }
        return key to value
    }

    private fun getAppliedFilterMap(options: List<Option>): Map<String, String> {
        return options.groupBy { it.key }.mapValues {
            it.value.filter { option -> option.inputState.toBoolean() }
                .joinToString(TokofoodFilterSortMapper.OPTION_SEPARATOR) { optionValue ->
                    optionValue.value
                }
        }
    }

    private fun loadDetailSortFilter() {
        launchCatchError(
            block = {
                val dataValue = withContext(dispatcher.io) {
                    tokofoodFilterSortUseCase.execute(TokofoodFilterSortUseCase.TYPE_DETAIL)
                }
                val dynamicFilterModelResult = DynamicFilterModel(dataValue)
                dynamicFilterModel.value = dynamicFilterModelResult
                _uiEventFlow.emit(
                    TokofoodSearchUiEvent(
                        state = TokofoodSearchUiEvent.EVENT_SUCCESS_LOAD_DETAIL_FILTER,
                        data = dynamicFilterModelResult
                    )
                )
            },
            onError = {
                _uiEventFlow.emit(
                    TokofoodSearchUiEvent(
                        state = TokofoodSearchUiEvent.EVENT_FAILED_LOAD_DETAIL_FILTER,
                        throwable = it
                    )
                )
            }
        )
    }

    private fun loadMoreSearch() {
        launchCatchError(
            block = {
                val searchResult = getSearchResult()
                pageKeyLiveData.value = searchResult.tokofoodSearchMerchant.nextPageKey
                _uiState.emit(getMoreSearchResultSuccessState(searchResult))
            },
            onError = {
                _uiEventFlow.emit(
                    TokofoodSearchUiEvent(
                        state = TokofoodSearchUiEvent.EVENT_FAILED_LOAD_MORE,
                        throwable = it
                    )
                )
            }
        )
    }

    private suspend fun getSearchResult(): TokofoodSearchMerchantResponse {
        return withContext(dispatcher.io) {
            val localCacheModel = localCacheModelLiveData.value
            val pageKey = pageKeyLiveData.value
            val searchParameter = getCurrentSearchParameter()
            tokofoodSearchMerchantUseCase.execute(localCacheModel, searchParameter, pageKey)
        }
    }

    private fun getCurrentSearchParameter(): SearchParameter {
        return currentSearchParameter.value ?: SearchParameter()
    }

    private fun shouldLoadMore(lastVisibleItemIndex: Int, itemCount: Int): Boolean {
        val lastItemIndex = itemCount - Int.ONE
        val scrolledToLastItem = (lastVisibleItemIndex == lastItemIndex
                && lastVisibleItemIndex.isMoreThanZero())
        val hasNextPage = !pageKeyLiveData.value.isNullOrEmpty()
        val containsOtherState = getIsVisitableContainOtherStates()

        return scrolledToLastItem && hasNextPage && !containsOtherState
    }

    /**
     * Checks whether the visitable has other models other than merchant item
     *
     * @return  isVisitableContainsOtherStates
     */
    private fun getIsVisitableContainOtherStates(): Boolean {
        val layoutList = currentVisitables.value.orEmpty()
        return layoutList.find {
            it is TokoFoodProgressBarUiModel || it is TokoFoodErrorStateUiModel ||
                    it is MerchantSearchEmptyWithFilterUiModel || it is MerchantSearchEmptyWithoutFilterUiModel
        } != null
    }

    private fun getInitialSearchResultSuccessState(searchResult: TokofoodSearchMerchantResponse): TokofoodSearchUiState {
        val currentSearchParameter = getCurrentSearchParameter()
        return when {
            searchResult.tokofoodSearchMerchant.merchants.isEmpty() && currentSearchParameter.hasFilterSortApplied() -> {
                TokofoodSearchUiState(state = TokofoodSearchUiState.STATE_EMPTY_WITH_FILTER)
            }
            searchResult.tokofoodSearchMerchant.merchants.isEmpty() -> {
                TokofoodSearchUiState(state = TokofoodSearchUiState.STATE_EMPTY_WITHOUT_FILTER)
            }
            else -> {
                TokofoodSearchUiState(
                    state = TokofoodSearchUiState.STATE_SUCCESS_LOAD_INITIAL,
                    data = searchResult
                )
            }
        }
    }

    private fun getMoreSearchResultSuccessState(searchResult: TokofoodSearchMerchantResponse): TokofoodSearchUiState {
        return TokofoodSearchUiState(
            state = TokofoodSearchUiState.STATE_SUCCESS_LOAD_MORE,
            data = searchResult
        )
    }

    private fun SearchParameter.hasFilterSortApplied(): Boolean {
        return if (contains(SearchApiConst.Q)) {
            getSearchParameterHashMap().entries.size > Int.ONE
        } else {
            getSearchParameterMap().entries.size > Int.ZERO
        }
    }

    private fun getVisitablesFlow(uiState: TokofoodSearchUiState): Flow<Result<List<Visitable<*>>>> {
        return flow {
            val visitables =
                when (uiState.state) {
                    TokofoodSearchUiState.STATE_LOAD_INITIAL -> {
                        getLoadingInitialState()
                    }
                    TokofoodSearchUiState.STATE_LOAD_MORE -> {
                        getLoadingMoreState()
                    }
                    TokofoodSearchUiState.STATE_EMPTY_WITHOUT_FILTER -> {
                        getEmptyWithoutFilterState()
                    }
                    TokofoodSearchUiState.STATE_EMPTY_WITH_FILTER -> {
                        getEmptyWithFilterState()
                    }
                    TokofoodSearchUiState.STATE_SUCCESS_LOAD_INITIAL -> {
                        getSuccessLoadSearchResultInitial(uiState)
                    }
                    TokofoodSearchUiState.STATE_SUCCESS_LOAD_MORE -> {
                        getSuccessLoadSearchResultMore(uiState)
                    }
                    TokofoodSearchUiState.STATE_ERROR_INITIAL -> {
                        getErrorSearchResultInitial(uiState)
                    }
                    else -> {
                        listOf()
                    }
                }
            currentVisitables.value = visitables
            emit(Success(visitables))
        }
    }

    private fun getLoadingInitialState(): List<Visitable<*>> {
        return listOf(TokoFoodCategoryLoadingStateUiModel(""))
    }

    private fun getLoadingMoreState(): List<Visitable<*>> {
        return tokofoodMerchantSearchResultMapper.getLoadMoreVisitables(currentVisitables.value)
    }

    private fun getEmptyWithoutFilterState(): List<Visitable<*>> {
        return listOf(MerchantSearchEmptyWithoutFilterUiModel(pageKeyLiveData.value.orEmpty()))
    }

    private fun getEmptyWithFilterState(): List<Visitable<*>> {
        return listOf(MerchantSearchEmptyWithFilterUiModel)
    }

    private fun getSuccessLoadSearchResultInitial(uiState: TokofoodSearchUiState): List<Visitable<*>> {
        return (uiState.data as? TokofoodSearchMerchantResponse)?.let { response ->
            tokofoodMerchantSearchResultMapper.mapResponseToVisitables(response)
        }.orEmpty()
    }

    private fun getSuccessLoadSearchResultMore(uiState: TokofoodSearchUiState): List<Visitable<*>> {
        return (uiState.data as? TokofoodSearchMerchantResponse)?.let { response ->
            currentVisitables.value.orEmpty() + tokofoodMerchantSearchResultMapper.mapResponseToVisitables(response)
        }.orEmpty()
    }

    private fun getErrorSearchResultInitial(uiState: TokofoodSearchUiState): List<Visitable<*>> {
        return uiState.throwable?.let {
            listOf(TokoFoodErrorStateUiModel(String.EMPTY, it))
        }.orEmpty()
    }

    private fun showQuickSortBottomSheet(sortList: List<Sort>,
                                         selectedSortValue: String) {
        _uiEventFlow.tryEmit(
            TokofoodSearchUiEvent(
                state = TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET,
                data = tokofoodFilterSortMapper.getQuickSortUiModels(sortList, selectedSortValue)
            )
        )
    }

    private fun getQuickFilterBottomSheetUiEvent(filter: Filter): TokofoodSearchUiEvent {
        return if (filter.isPriceRangeCbFilter) {
            TokofoodSearchUiEvent(
                state = TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_PRICE_RANGE_BOTTOMSHEET,
                data = tokofoodFilterSortMapper.getQuickFilterPriceRangeUiModels(filter)
            )
        } else {
            TokofoodSearchUiEvent(
                state = TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_NORMAL_BOTTOMSHEET,
                data = filter
            )
        }
    }

    companion object {
        private const val MIN_SEARCH_KEYWORD_LENGTH = 3

        private const val SHARING_DELAY_MILLIS = 5000L
    }

}