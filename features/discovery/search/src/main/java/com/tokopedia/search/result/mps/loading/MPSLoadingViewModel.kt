package com.tokopedia.search.result.mps.loading

import com.tokopedia.search.utils.mvvm.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MPSLoadingViewModel(
    initialState: MPSLoadingState,
    private val scope: CoroutineScope,
    private val config: MPSLoadingConfig,
): SearchViewModel<MPSLoadingState> {

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow = _stateFlow.asStateFlow()

    private val state
        get() = stateFlow.value

    fun start() {
        scope.launch {
            while (state.isBelowPauseThreshold(config.pauseProgressThreshold)) {
                delay(config.delay)
                _stateFlow.update { it.incrementLoading(config.randomIncrement()) }
            }
        }
    }

    fun finish() {
        _stateFlow.update { it.finish() }
    }
}
