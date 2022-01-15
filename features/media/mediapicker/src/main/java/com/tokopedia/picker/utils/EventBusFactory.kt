package com.tokopedia.picker.utils

import com.tokopedia.picker.data.entity.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

sealed class EventState {
    object Idle: EventState()
    class MediaSelection(val data: List<Media>): EventState()
    class MediaRemoved(val media: Media?): EventState()
}

object EventBusFactory {

    private val state = MutableStateFlow<EventState>(EventState.Idle)

    fun send(stateEvent: EventState) {
        state.tryEmit(stateEvent)
    }

    suspend fun consumer(
        coroutineContext: CoroutineContext,
        stateCallback: (EventState) -> Unit
    ) {
        state.debounce(0)
            .flowOn(coroutineContext)
            .collectLatest {
                withContext(Dispatchers.Main) {
                    stateCallback(it)
                }
            }
    }

}