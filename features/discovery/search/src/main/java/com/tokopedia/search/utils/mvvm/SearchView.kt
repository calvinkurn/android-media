package com.tokopedia.search.utils.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlin.reflect.KProperty1

interface SearchView: LifecycleOwner {

    fun <S: SearchUiState, P> SearchViewModel<S>.onEach(
        prop: KProperty1<S, P>,
        action: suspend (P) -> Unit,
    ) = onEach(
        lifecycleScope,
        prop,
        action
    )

    fun <S: SearchUiState> SearchViewModel<S>.observeState() = onState(lifecycleScope) {
        refresh()
    }

    fun <VM: SearchViewModel<S>, S: SearchUiState> withState(
        viewModel: VM?,
        action: (S) -> Unit,
    ) {
        viewModel?.let { action(it.stateFlow.value) }
    }

    fun refresh()
}
