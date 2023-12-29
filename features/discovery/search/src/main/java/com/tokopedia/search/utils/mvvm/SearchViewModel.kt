package com.tokopedia.search.utils.mvvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

interface SearchViewModel<S: SearchUiState> {

    val stateFlow: StateFlow<S>

    fun onState(
        scope: CoroutineScope,
        action: suspend (S) -> Unit,
    ) = stateFlow
        .resolve(scope, action)

    fun <P> onEach(
        scope: CoroutineScope,
        prop: KProperty1<S, P>,
        action: suspend (P) -> Unit,
    ) = stateFlow
        .map { prop.get(it) }
        .distinctUntilChanged()
        .resolve(scope, action)

    private fun <T> Flow<T>.resolve(
        scope: CoroutineScope,
        action: suspend (T) -> Unit,
    ): Job =
        scope.launch {
            collectLatest(action)
        }
}
