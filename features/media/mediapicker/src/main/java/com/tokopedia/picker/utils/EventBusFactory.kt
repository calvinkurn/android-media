package com.tokopedia.picker.utils

import com.tokopedia.picker.ui.uimodel.MediaUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

sealed class EventState {
    object Idle: EventState()
    class CameraCaptured(val data: MediaUiModel?): EventState()
    class SelectionChanged(val data: List<MediaUiModel>): EventState()
    class SelectionRemoved(val media: MediaUiModel?, val data: List<MediaUiModel>): EventState()
}

object EventBusFactory {

    val state = MutableSharedFlow<EventState>(1)

    init {
        state.distinctUntilChanged()
        state.tryEmit(EventState.Idle)
    }

    fun emit(stateEvent: EventState) {
        state.tryEmit(stateEvent)
    }

    fun subscriber(coroutineScope: CoroutineScope): Flow<EventState> {
        return state.shareIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(500),
            1
        )
    }

}