package com.tokopedia.picker.utils

import com.tokopedia.picker.data.entity.Media
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlin.coroutines.CoroutineContext

sealed class EventChannelState {
    object Idle: EventChannelState()
    class SelectedMedia(
        val medias: List<Media>
    ): EventChannelState()
}

object EventPublisher : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    @Suppress("ObjectPropertyName")
    private val _state = MutableStateFlow<EventChannelState>(EventChannelState.Idle)
    private val state = _state.asStateFlow()

    fun send(stateEvent: EventChannelState) {
        _state.tryEmit(stateEvent)
    }

    fun consumer(stateCallback: (EventChannelState) -> Unit) {
        launch {
            state.debounce(500)
                .collect {
                    withContext(Dispatchers.Main) {
                        stateCallback(it)
                    }
                }
        }
    }

    fun clear() {
        coroutineContext.cancelChildren()
    }

}