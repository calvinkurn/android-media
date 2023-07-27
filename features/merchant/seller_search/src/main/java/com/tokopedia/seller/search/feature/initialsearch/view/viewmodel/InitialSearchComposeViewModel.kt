package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.mapper.GlobalSearchSellerMapper
import com.tokopedia.seller.search.feature.initialsearch.domain.usecase.DeleteSuggestionHistoryUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _uiEvent = MutableSharedFlow<InitialSearchUiEvent>(replay = Int.ONE)
    val uiEvent get() = _uiEvent.asSharedFlow()

    fun onUiEvent(event: InitialSearchUiEvent) {
        viewModelScope.launch {
            when (event) {
                is InitialSearchUiEvent.OnItemHighlightClicked -> {
                    insertSearchSeller(
                        event.item.title.orEmpty(),
                        event.item.id.orEmpty(),
                        event.item.title.orEmpty(),
                        event.position
                    )
                    _uiEvent.emit(
                        InitialSearchUiEvent.OnItemHighlightClickedAction(
                            event.item,
                            event.position
                        )
                    )
                }

                is InitialSearchUiEvent.OnClearAllHistory -> {
                    deleteSuggestionSearch(event.titleList, null)
                    _uiEvent.emit(InitialSearchUiEvent.OnClearAllHistoryAction(event.titleList))
                }

                is InitialSearchUiEvent.OnItemRemoveClicked -> {
                    deleteSuggestionSearch(listOf(event.title), event.position)
                    _uiEvent.emit(
                        InitialSearchUiEvent.OnItemRemoveClickedAction(
                            event.title,
                            event.position
                        )
                    )
                }

                else -> {
                    _uiEvent.emit(event)
                }
            }
        }
    }

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
        }, onError = {
                _uiState.update {
                    it.copy(throwable = it.throwable)
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
        }, onError = {
                _uiState.update {
                    it.copy(throwable = it.throwable)
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
                it.copy(isInsertSearchSuccess = true)
            }
        }, onError = {
                _uiState.update {
                    it.copy(isInsertSearchSuccess = true, throwable = it.throwable)
                }
            })
    }
}
