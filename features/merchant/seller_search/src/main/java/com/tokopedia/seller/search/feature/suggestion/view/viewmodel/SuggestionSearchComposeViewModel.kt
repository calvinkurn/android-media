package com.tokopedia.seller.search.feature.suggestion.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.mapper.GlobalSearchSellerMapper
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SuggestionSearchComposeViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSellerSearchUseCase: GetSellerSearchUseCase,
    private val insertSellerSearchUseCase: InsertSuccessSearchUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val _uiState = MutableStateFlow(SuggestionSearchUiState())
    val uiState: StateFlow<SuggestionSearchUiState>
        get() = _uiState

    fun showLoading() {
        _uiState.update {
            it.copy(isLoadingState = true)
        }
    }

    fun fetchSellerSearch(keyword: String, section: String = "", shopId: String) {
        launchCatchError(block = {
            val responseGetSellerSearch = withContext(dispatcherProvider.io) {
                getSellerSearchUseCase.params =
                    GetSellerSearchUseCase.createParams(keyword, shopId, section)
                val sellerSearchVisitableList = GlobalSearchSellerMapper.mapToSellerSearchVisitable(
                    getSellerSearchUseCase.executeOnBackground(),
                    keyword
                )
                GlobalSearchSellerMapper.mapToSuggestionSellerSearchVisitable(sellerSearchVisitableList)
            }

            _uiState.update {
                it.copy(
                    suggestionSellerSearchList = responseGetSellerSearch,
                    isLoadingState = false
                )
            }
        }, onError = { throwable ->
                _uiState.update {
                    it.copy(throwable = throwable, isLoadingState = false)
                }
            })
    }

    fun insertSearchSeller(keyword: String, id: String, title: String, index: Int) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                insertSellerSearchUseCase.params = InsertSuccessSearchUseCase.createParams(
                    keyword,
                    id,
                    title,
                    index
                )
                insertSellerSearchUseCase.executeOnBackground()
            }
            _uiState.update {
                it.copy(isInsertSuccessSearch = true)
            }
        }, onError = { throwable ->
                _uiState.update {
                    it.copy(throwable = throwable, isInsertSuccessSearch = false)
                }
            })
    }
}
