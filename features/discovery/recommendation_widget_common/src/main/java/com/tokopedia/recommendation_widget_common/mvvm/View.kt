package com.tokopedia.recommendation_widget_common.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

internal interface View: LifecycleOwner {

    fun launchRepeatOnLifecycleStarted(
        block: suspend CoroutineScope.() -> Unit,
    ) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }

    fun <S: UiState, P> ViewModel<S>.onEach(
        prop: KProperty1<S, P>,
        action: suspend (P) -> Unit,
    ) = launchRepeatOnLifecycleStarted {
        stateFlow
            .map { prop.get(it) }
            .distinctUntilChanged()
            .collectLatest(action)
    }

    fun <S: UiState> ViewModel<S>.observeState() =
        launchRepeatOnLifecycleStarted {
            stateFlow.collectLatest { refresh() }
        }

    fun <VM: ViewModel<S>, S: UiState> withState(
        viewModel: VM?,
        action: (S) -> Unit,
    ) {
        viewModel?.let { action(it.stateFlow.value) }
    }

    fun refresh()
}
