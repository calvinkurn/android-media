package com.tokopedia.picker.utils

import com.tokopedia.picker.data.entity.Media
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

sealed class EventState {
    object Idle: EventState()
    class CameraCaptured(val data: Media?): EventState()
    class SelectionChanged(val data: List<Media>): EventState()
    class SelectionRemoved(val media: Media?, val data: List<Media>): EventState()
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