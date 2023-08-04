package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.seller.search.common.domain.GetSellerSearchPlaceholderUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class)
class InitialSearchActivityComposeViewModel @Inject constructor(
    private val getPlaceholderUseCase: GetSellerSearchPlaceholderUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _globalSearchUiState = MutableStateFlow(GlobalSearchUiState())
    val globalSearchUiState: StateFlow<GlobalSearchUiState>
        get() = _globalSearchUiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<GlobalSearchUiEvent>(replay = Int.ONE)
    val uiEffect get() = _uiEffect.asSharedFlow()

    private val searchTypingStateFlow = MutableStateFlow(String.EMPTY)

    init {
        viewModelScope.launch {
            searchTypingStateFlow
                .debounce(DEBOUNCE_DELAY_MILLIS)
                .distinctUntilChanged()
                .collectLatest { keyword ->
                    _uiEffect.emit(GlobalSearchUiEvent.OnSearchResultKeyword(keyword))
                }
        }
    }

    fun onUiEffect(event: GlobalSearchUiEvent) {
        viewModelScope.launch {
            when (event) {
                is GlobalSearchUiEvent.OnKeywordTextChanged -> {
                    setTypingSearch(event.searchBarKeyword)
                }
                else -> {
                    _uiEffect.emit(event)
                }
            }
        }
    }

    fun getSearchPlaceholder() {
        launchCatchError(block = {
            val placeholder = withContext(dispatchers.io) {
                getPlaceholderUseCase.executeOnBackground()
                    .response
                    .sentence
            }
            _globalSearchUiState.update {
                it.copy(searchBarPlaceholder = placeholder)
            }
        }) {
            _globalSearchUiState.update {
                it.copy(searchBarPlaceholder = String.EMPTY)
            }
        }
    }

    fun setTypingSearch(keyword: String) {
        _globalSearchUiState.update {
            it.copy(searchBarKeyword = keyword)
        }
        searchTypingStateFlow.tryEmit(keyword)
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}
