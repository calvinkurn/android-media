package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    private val _searchQuery = MutableSharedFlow<String>(Int.ONE)
    private val _uiState = MutableSharedFlow<TokofoodSearchUiState>(Int.ONE)

    @FlowPreview
    val sortFilterUiModel = MutableSharedFlow<Result<List<TokofoodSortFilterItemUiModel>>>(Int.ONE)

    val searchParameters: SharedFlow<SearchParameter?> =
        combine(_searchKeyword, _searchQuery) { keyword, query ->
            if (keyword.isNotEmpty() || query.isNotEmpty()) {
                val searchParameter = SearchParameter(query)
                if (keyword.length >= MIN_SEARCH_KEYWORD_LENGTH) {
                    searchParameter.set(SearchApiConst.Q, keyword)
                }
                searchParameter
            } else {
                null
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(5000L),
            replay = Int.ONE
        )

    @FlowPreview
    val visitables: SharedFlow<Result<List<Visitable<*>>>> =
        _uiState.flatMapConcat { uiState ->
            getVisitablesFlow(uiState)
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(5000L),
            replay = Int.ONE
        )

    private val localCacheModelLiveData = MutableLiveData<LocalCacheModel>()
    private val pageKeyLiveData = MutableLiveData<String>()
    private val currentVisitables = MutableLiveData<List<Visitable<*>>>()
    private val currentSearchParameter = MutableLiveData<SearchParameter>()

    fun setKeyword(keyword: String) {
        _searchKeyword.tryEmit(keyword)
        _searchQuery.tryEmit("")
    }

    fun setLocalCacheModel(localCacheModel: LocalCacheModel) {
        localCacheModelLiveData.value = localCacheModel
    }

    fun loadSortFilter() {
        launchCatchError(
            block = {
                val dataValue = withContext(dispatcher.io) {
                    tokofoodFilterSortUseCase.execute("quick")
                }
                val uiModels = tokofoodFilterSortMapper.getQuickSortFilterUiModels(dataValue)
                sortFilterUiModel.emit(Success(uiModels))
            },
            onError = {
                sortFilterUiModel.emit(Fail(it))
            }
        )
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

    private fun loadMoreSearch() {
        launchCatchError(
            block = {
                val searchResult = getSearchResult()
                pageKeyLiveData.value = searchResult.tokofoodSearchMerchant.nextPageKey
                _uiState.emit(getMoreSearchResultSuccessState(searchResult))
            },
            onError = {
                _uiState.emit(
                    TokofoodSearchUiState(
                        state = TokofoodSearchUiState.STATE_ERROR_LOAD_MORE,
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

    companion object {
        private const val MIN_SEARCH_KEYWORD_LENGTH = 3
    }

}