package com.tokopedia.picker.utils

import com.tokopedia.picker.ui.uimodel.MediaUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface AddMediaEvent {
    val data: MediaUiModel?
}

sealed class EventState {
    object Idle: EventState()

    class CameraCaptured(
        override val data: MediaUiModel?
    ) : EventState(), AddMediaEvent

    class SelectionAdded(
        override val data: MediaUiModel?
    ) : EventState(), AddMediaEvent

    class SelectionChanged(val data: List<MediaUiModel>): EventState()

    class SelectionRemoved(val media: MediaUiModel): EventState()
}

object EventBusFactory {

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