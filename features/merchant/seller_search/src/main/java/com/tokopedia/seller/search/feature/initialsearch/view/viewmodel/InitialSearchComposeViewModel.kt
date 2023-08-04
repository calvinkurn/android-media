package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.mapper.GlobalSearchSellerMapper
import com.tokopedia.seller.search.feature.initialsearch.domain.usecase.DeleteSuggestionHistoryUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InitialSearchComposeViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSellerSearchUseCase: GetSellerSearchUseCase,
    private val deleteSuggestionHistoryUseCase: DeleteSuggestionHistoryUseCase,
    private val insertSellerSearchUseCase: InsertSuccessSearchUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val _uiState = MutableStateFlow(InitialSearchUiState())
    val uiState: StateFlow<InitialSearchUiState>
        get() = _uiState.asStateFlow()

    fun fetchSellerSearch(keyword: String, section: String = "", shopId: String) {
        launchCatchError(block = {
            val responseGetSellerSearch = withContext(dispatcherProvider.io) {
                getSellerSearchUseCase.params = GetSellerSearchUseCase.createParams(
                    keyword,
                    shopId,
                    section
                )
                GlobalSearchSellerMapper.mapToInitialSearchVisitableCompose(getSellerSearchUseCase.executeOnBackground())
            }
            _uiState.update {
                it.copy(
                    initialStateList = responseGetSellerSearch.first,
                    titleList = responseGetSellerSearch.second
                )
            }
        }, onError = { throwable ->
                _uiState.update {
                    it.copy(throwable = throwable)
                }
            })
    }

    fun deleteSuggestionSearch(keyword: List<String>, itemPosition: Int?) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                deleteSuggestionHistoryUseCase.params =
                    DeleteSuggestionHistoryUseCase.createParams(keyword)
                deleteSuggestionHistoryUseCase.executeOnBackground()
            }

            val initialStateList = _uiState.value.initialStateList

            val updateInitialStateList = if (itemPosition == null) {
                GlobalSearchSellerMapper.mapToDeleteAllSuggestionSearch(initialStateList)
            } else {
                GlobalSearchSellerMapper.mapToDeleteItemSuggestionSearch(
                    initialStateList,
                    itemPosition
                )
            }

            _uiState.update {
                it.copy(initialStateList = updateInitialStateList)
            }
        }, onError = { throwable ->
                _uiState.update {
                    it.copy(throwable = throwable)
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
                it.copy(isDismissKeyboard = true)
            }
        }, onError = { throwable ->
                _uiState.update {
                    it.copy(isDismissKeyboard = true, throwable = throwable)
                }
            })
    }
}
