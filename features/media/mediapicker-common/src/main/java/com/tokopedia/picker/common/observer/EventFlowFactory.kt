package com.tokopedia.picker.common.observer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

object EventFlowFactory {
    private const val TIMEOUT_IN_MILLIS = 500L
    private const val MAX_REPLAY = 50

    private val stateList = mutableMapOf<String, MutableSharedFlow<EventState>>()

    fun emit(stateEvent: EventState) {
        state(stateEvent.key).tryEmit(stateEvent)
    }

    fun subscriber(coroutineScope: CoroutineScope, key: String): Flow<EventState> {
        return state(key).shareIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(TIMEOUT_IN_MILLIS),
            MAX_REPLAY
        ).onCompletion {
            emit(EventState.Idle)
        }
    }

    fun reset(key: String) {
        stateList.remove(key)
    }

    fun dispose(key: String) {
        state(key).tryEmit(EventState.Idle)
    }

    private fun state(key: String): MutableSharedFlow<EventState> {
        stateList[key]?.let {
            return it
        }

        val newState = MutableSharedFlow<EventState>(MAX_REPLAY)
        stateList[key] = newState
        return newState
    }
}
