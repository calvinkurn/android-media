package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodSearchMerchantUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
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

    fun getInitialMerchantSearchResult(searchParameter: SearchParameter) {
        pageKeyLiveData.value = null
        _uiState.tryEmit(
            TokofoodSearchUiState(
                state = TokofoodSearchUiState.STATE_LOAD_INITIAL
            )
        )
        launchCatchError(
            block = {
                val searchResult = withContext(dispatcher.io) {
                    val localCacheModel = localCacheModelLiveData.value
                    val pageKey = pageKeyLiveData.value
                    tokofoodSearchMerchantUseCase.execute(localCacheModel, searchParameter, pageKey)
                }
                _uiState.emit(
                    TokofoodSearchUiState(
                        state = TokofoodSearchUiState.STATE_SUCCESS_LOAD_INITIAL,
                        data = searchResult
                    )
                )
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

    private fun getVisitablesFlow(uiState: TokofoodSearchUiState): Flow<Result<List<Visitable<*>>>> {
        return flow {
            val visitables =
                when (uiState.state) {
                    TokofoodSearchUiState.STATE_LOAD_INITIAL -> {
                        getLoadingStateInitial()
                    }
                    TokofoodSearchUiState.STATE_SUCCESS_LOAD_INITIAL -> {
                        getSuccessLoadSearchResultInitial(uiState)
                    }
                    else -> {
                        listOf()
                    }
                }
            emit(Success(visitables))
        }
    }

    private fun getLoadingStateInitial(): List<Visitable<*>> {
        return listOf(TokoFoodCategoryLoadingStateUiModel(""))
    }

    private fun getSuccessLoadSearchResultInitial(uiState: TokofoodSearchUiState): List<Visitable<*>> {
        return (uiState.data as? TokofoodSearchMerchantResponse)?.let { response ->
            tokofoodMerchantSearchResultMapper.mapResponseToVisitables(response)
        }.orEmpty()
    }

    companion object {
        private const val MIN_SEARCH_KEYWORD_LENGTH = 3
    }

}