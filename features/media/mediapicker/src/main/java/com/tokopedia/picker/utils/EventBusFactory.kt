package com.tokopedia.picker.utils

import com.tokopedia.picker.data.entity.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

sealed class EventState {
    object Idle: EventState()
    class CameraCaptured(val data: Media?): EventState()
    class SelectionChanged(val data: List<Media>): EventState()
    class SelectionRemoved(val media: Media?, val data: List<Media>): EventState()
}

object EventBusFactory {

    private val state = MutableStateFlow<EventState>(EventState.Idle)

    fun send(stateEvent: EventState) {
        state.tryEmit(stateEvent)
    }

    suspend fun consumer(stateCallback: (EventState) -> Unit) {
        state.collectLatest {
            withContext(Dispatchers.Main) {
                stateCallback(it)
            }
        }
    }

}