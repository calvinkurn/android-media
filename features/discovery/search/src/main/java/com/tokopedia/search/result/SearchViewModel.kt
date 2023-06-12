package com.tokopedia.search.result

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.tokopedia.search.utils.mvvm.SearchViewModel as ISearchViewModel

internal class SearchViewModel(
    searchState: SearchState,
    coroutineDispatcher: CoroutineDispatchers,
): BaseViewModel(coroutineDispatcher.main), ISearchViewModel<SearchState> {

    private val _stateFlow = MutableStateFlow(searchState)

    override val stateFlow: StateFlow<SearchState>
        get() = _stateFlow.asStateFlow()

    fun showAutoCompleteView() {
        _stateFlow.update { it.openAutoComplete() }
    }

    fun showAutoCompleteHandled() {
        _stateFlow.update { it.openAutoCompleteHandled() }
    }

    fun setActiveTab(position: Int) {
        _stateFlow.update { it.activeTab(position) }
    }
}
