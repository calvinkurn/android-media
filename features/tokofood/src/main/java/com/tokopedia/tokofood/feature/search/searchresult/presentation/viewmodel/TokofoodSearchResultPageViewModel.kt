package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper.getActiveCount
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper.hasFilterSortApplied
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper.resetParams
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper.setAppliedInputState
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodSearchMerchantUseCase
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
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
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
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

    val searchParameterMap: SharedFlow<HashMap<String, String>> =
        combine(_searchKeyword, _searchMap) { keyword, map ->
            val searchParameter = currentSearchParameterMap.value ?: hashMapOf()
            map.entries.forEach {
                searchParameter[it.key] = it.value
            }
            if (keyword.length >= MIN_SEARCH_KEYWORD_LENGTH) {
                searchParameter[SearchApiConst.Q] = keyword
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
    private val currentSearchParameterMap = MutableLiveData<HashMap<String, String>>()
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

    fun loadQuickSortFilter(searchParameter: HashMap<String, String>) {
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
                    sendFailureEvent(TokofoodSearchUiEvent.EVENT_FAILED_LOAD_FILTER, it)
                    _sortFilterUiModel.emit(Fail(it))
                }
            )
        }
    }

    fun resetFilterSearch() {
        currentSearchParameterMap.value = null
        currentSortFilterUiModels.value = null
        _searchMap.tryEmit(hashMapOf())
    }

    fun getInitialMerchantSearchResult(searchParameter: HashMap<String, String>?) {
        if (searchParameter != null) {
            pageKeyLiveData.value = null
            currentSearchParameterMap.value = searchParameter
            showInitialLoading()
            launchCatchError(
                block = {
                    checkAddressForEligibility {
                        val searchResult = getSearchResult()
                        emitSuccessIfInCoverage(searchResult) {
                            _uiState.emit(getInitialSearchResultSuccessState(searchResult))
                        }
                    }
                },
                onError = {
                    _uiState.emit(
                        TokofoodSearchUiState(
                            state = TokofoodSearchUiState.STATE_ERROR_INITIAL,
                            throwable = it
                        )
                    )
                    sendFailureEvent(TokofoodSearchUiEvent.EVENT_FAILED_LOAD_SEARCH_RESULT, it)
                }
            )
        }
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
        val dynamicFilterModelData = dynamicFilterModel.value?.let {
            getDynamicFilterModel(it.data)
        }
        _uiEventFlow.tryEmit(
            TokofoodSearchUiEvent(
                state = TokofoodSearchUiEvent.EVENT_OPEN_DETAIL_BOTTOMSHEET,
                data = dynamicFilterModelData
            )
        )
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
        currentSearchParameterMap.value?.run {
            set(sort.key, value)
            _searchMap.tryEmit(this)
        }
    }

    fun applySortSelected(uiModel: TokofoodQuickSortUiModel) {
        currentSearchParameterMap.value?.run {
            set(uiModel.key, uiModel.value)
            _searchMap.tryEmit(this)
        }
    }

    fun applyFilter(filter: Filter) {
        currentSearchParameterMap.value?.run {
            getAppliedFilterMap(filter)?.let { (key, value) ->
                set(key, value)
            }

            _searchMap.tryEmit(this)
        }
    }

    fun applyOptions(options: List<Option>) {
        currentSearchParameterMap.value?.run {
            getAppliedFilterMap(options).entries.forEach {
                set(it.key, it.value)
            }

            _searchMap.tryEmit(this)
        }
    }

    fun resetParams(queryParams: Map<String, String>) {
        currentSearchParameterMap.value?.run {
            resetParams(queryParams)
            _searchMap.tryEmit(this)
        }
    }

    fun showQuickSortBottomSheet(sort: List<Sort>) {
        val firstSort = sort.firstOrNull()
        val sortKey = firstSort?.key
        sortKey?.let { currentKey ->
            val currentParameters = currentSearchParameterMap.value
            val currentValue = currentParameters?.get(currentKey)
            showQuickSortBottomSheet(sort, currentValue.orEmpty())
        }
    }

    fun showQuickFilterBottomSheet(filter: Filter) {
        val uiEvent = getQuickFilterBottomSheetUiEvent(filter)
        _uiEventFlow.tryEmit(uiEvent)
    }

    fun getCurrentSortValue(): String {
        val sortKey = tokofoodFilterSortMapper.getCurrentSortKey(currentSortFilterUiModels.value)
        return currentSearchParameterMap.value?.get(sortKey).orEmpty()
    }

    fun updatePinpoint(latitude: String, longitude: String) {
        showInitialLoading()
        val localCacheModel = localCacheModelLiveData.value
        val addressId = localCacheModel?.address_id
        if (addressId.isNullOrBlank()) {
            checkEligibleForAnaRevamp()
        } else {
            editPinpoint(addressId, latitude, longitude)
        }
    }

    private fun setIndicatorCount() {
        currentSearchParameterMap.value?.let { searchParameter ->
            _appliedFilterCount.tryEmit(searchParameter.getActiveCount())
        }
    }

    private fun shouldLoadSortFilter(searchParameter: HashMap<String, String>): Boolean {
        return !getIsSortFilterLoaded() || isSearchParameterNew(searchParameter)
    }

    private fun getIsSortFilterLoaded(): Boolean {
        return !currentSortFilterUiModels.value.isNullOrEmpty()
    }

    private fun isSearchParameterNew(searchParameter: HashMap<String, String>): Boolean {
        return currentSearchParameterMap.value == searchParameter
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

    private fun showInitialLoading() {
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_LOAD_INITIAL
            )
        )
    }

    private fun loadDetailSortFilter() {
        launchCatchError(
            block = {
                val dataValue = withContext(dispatcher.io) {
                    tokofoodFilterSortUseCase.execute(TokofoodFilterSortUseCase.TYPE_DETAIL)
                }
                val dynamicFilterModelResult = getDynamicFilterModel(dataValue)
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
                checkAddressForEligibility {
                    val searchResult = getSearchResult()
                    emitSuccessIfInCoverage(searchResult) {
                        pageKeyLiveData.value = searchResult.tokofoodSearchMerchant.nextPageKey
                        _uiState.emit(getMoreSearchResultSuccessState(searchResult))
                    }
                }
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
            val searchParameter = TokofoodSearchResultHelper.getCurrentSearchParameter(currentSearchParameterMap.value)
            tokofoodSearchMerchantUseCase.execute(localCacheModel, searchParameter, pageKey)
        }
    }
    private fun shouldLoadMore(lastVisibleItemIndex: Int, itemCount: Int): Boolean {
        val lastItemIndex = itemCount - Int.ONE
        val scrolledToLastItem = (
            lastVisibleItemIndex == lastItemIndex &&
                lastVisibleItemIndex.isMoreThanZero()
            )
        val hasNextPage = TokofoodSearchResultHelper.getIsHasNextPage(pageKeyLiveData.value)
        val containsOtherState =
            tokofoodMerchantSearchResultMapper.getIsVisitableContainOtherStates(currentVisitables.value)

        return scrolledToLastItem && hasNextPage && !containsOtherState
    }

    private fun getInitialSearchResultSuccessState(searchResult: TokofoodSearchMerchantResponse): TokofoodSearchUiState {
        val currentSearchParameterMap = TokofoodSearchResultHelper.getCurrentSearchParameter(currentSearchParameterMap.value)
        pageKeyLiveData.value = searchResult.tokofoodSearchMerchant.nextPageKey
        return when {
            searchResult.tokofoodSearchMerchant.merchants.isEmpty() && currentSearchParameterMap.hasFilterSortApplied() -> {
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

    private suspend fun checkAddressForEligibility(onEligible: suspend () -> Unit) {
        localCacheModelLiveData.value.let {
            when {
                it == null || it.address_id.isBlank() -> {
                    checkEligibleForAnaRevamp()
                }
                it.latLong.isBlank() -> {
                    emitNoPinpointState()
                }
                else -> {
                    onEligible()
                }
            }
        }
    }

    private fun checkEligibleForAnaRevamp() {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                if (it.eligibleForRevampAna.eligible) {
                    emitNoAddressRevampState()
                } else {
                    emitNoAddressState()
                }
            },
            {
                emitNoAddressState()
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
    }

    private suspend fun emitSuccessIfInCoverage(
        response: TokofoodSearchMerchantResponse,
        onSuccess: suspend () -> Unit
    ) {
        if (response.tokofoodSearchMerchant.state.isOOC) {
            emitOutOfCoverageState()
        } else {
            onSuccess()
        }
    }

    private fun emitNoPinpointState() {
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_OOC,
                data = MerchantSearchOOCUiModel.NO_PINPOINT
            )
        )
    }

    private fun emitNoAddressState() {
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_OOC,
                data = MerchantSearchOOCUiModel.NO_ADDRESS
            )
        )
    }

    private fun emitNoAddressRevampState() {
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_OOC,
                data = MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP
            )
        )
    }

    private suspend fun emitOutOfCoverageState() {
        _uiState.emit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_OOC,
                data = MerchantSearchOOCUiModel.OUT_OF_COVERAGE
            )
        )
    }

    private fun getMoreSearchResultSuccessState(searchResult: TokofoodSearchMerchantResponse): TokofoodSearchUiState {
        return TokofoodSearchUiState(
            state = TokofoodSearchUiState.STATE_SUCCESS_LOAD_MORE,
            data = searchResult
        )
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
                        tokofoodMerchantSearchResultMapper.getSuccessLoadSearchResultInitial(uiState.data)
                    }
                    TokofoodSearchUiState.STATE_SUCCESS_LOAD_MORE -> {
                        tokofoodMerchantSearchResultMapper.getSuccessLoadSearchResultMore(
                            uiState.data,
                            currentVisitables.value
                        )
                    }
                    TokofoodSearchUiState.STATE_OOC -> {
                        tokofoodMerchantSearchResultMapper.getOutOfCoverageUiModels(uiState.data)
                    }
                    else -> {
                        tokofoodMerchantSearchResultMapper.getErrorSearchResultInitial(uiState.throwable)
                    }
                }
            currentVisitables.value = visitables
            emit(Success(visitables))
        }
    }

    private fun getLoadingInitialState(): List<Visitable<*>> {
        return listOf(TokoFoodCategoryLoadingStateUiModel(String.EMPTY))
    }

    private fun getLoadingMoreState(): List<Visitable<*>> {
        return tokofoodMerchantSearchResultMapper.getLoadMoreVisitables(currentVisitables.value)
    }

    private fun getEmptyWithoutFilterState(): List<Visitable<*>> {
        val currentKeyword = TokofoodSearchResultHelper.getCurrentKeyword(currentSearchParameterMap.value)
        return listOf(MerchantSearchEmptyWithoutFilterUiModel(currentKeyword))
    }

    private fun getEmptyWithFilterState(): List<Visitable<*>> {
        return listOf(MerchantSearchEmptyWithFilterUiModel)
    }

    private fun showQuickSortBottomSheet(
        sortList: List<Sort>,
        selectedSortValue: String
    ) {
        _uiEventFlow.tryEmit(
            TokofoodSearchUiEvent(
                state = TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET,
                data = tokofoodFilterSortMapper.getQuickSortUiModels(sortList, selectedSortValue)
            )
        )
    }

    private fun getQuickFilterBottomSheetUiEvent(filter: Filter): TokofoodSearchUiEvent {
        return if (filter.isPriceRangeCheckboxFilter) {
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

    private fun getDynamicFilterModel(dataValue: DataValue): DynamicFilterModel {
        val updatedDataValue = dataValue.apply {
            filter.forEach { filter ->
                filter.options.forEach { option ->
                    option.setAppliedInputState(currentSearchParameterMap.value)
                }
            }
        }
        return DynamicFilterModel(updatedDataValue)
    }

    private fun editPinpoint(
        addressId: String,
        latitude: String,
        longitude: String
    ) {
        launchCatchError(
            block = {
                val isSuccess = withContext(dispatcher.io) {
                    keroEditAddressUseCase.execute(addressId, latitude, longitude)
                }
                if (isSuccess) {
                    _uiEventFlow.tryEmit(
                        TokofoodSearchUiEvent(
                            state = TokofoodSearchUiEvent.EVENT_SUCCESS_EDIT_PINPOINT,
                            data = Pair(latitude, longitude)
                        )
                    )
                } else {
                    _uiEventFlow.tryEmit(
                        TokofoodSearchUiEvent(
                            state = TokofoodSearchUiEvent.EVENT_FAILED_EDIT_PINPOINT
                        )
                    )
                }
            },
            onError = {
                _uiEventFlow.tryEmit(
                    TokofoodSearchUiEvent(
                        state = TokofoodSearchUiEvent.EVENT_FAILED_EDIT_PINPOINT,
                        throwable = it
                    )
                )
            }
        )
    }

    private suspend fun sendFailureEvent(state: Int, throwable: Throwable) {
        _uiEventFlow.emit(
            TokofoodSearchUiEvent(
                state = state,
                throwable = throwable
            )
        )
    }

    companion object {
        private const val MIN_SEARCH_KEYWORD_LENGTH = 3

        private const val SHARING_DELAY_MILLIS = 5000L
    }
}
