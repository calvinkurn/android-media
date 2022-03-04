package com.tokopedia.media.common.observer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

object EventFlowFactory {

    val state = MutableSharedFlow<EventState>(1)
    private const val TIMEOUT_IN_MILLIS = 500L
    private const val MAX_REPLAY = 1

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

}