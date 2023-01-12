package com.tokopedia.picker.common.observer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

object EventFlowFactory {
    private const val TIMEOUT_IN_MILLIS = 500L
    private const val MAX_REPLAY = 50

    val state = MutableSharedFlow<EventState>(MAX_REPLAY)

    fun emit(stateEvent: EventState) {
        state.tryEmit(stateEvent)
    }

    fun subscriber(coroutineScope: CoroutineScope): Flow<EventState> {
        return state.shareIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(TIMEOUT_IN_MILLIS),
            MAX_REPLAY
        ).onCompletion {
            emit(EventState.Idle)
        }
    }

    fun reset() {
        state.resetReplayCache()
    }

    fun dispose() {
        state.tryEmit(EventState.Idle)
    }
}
