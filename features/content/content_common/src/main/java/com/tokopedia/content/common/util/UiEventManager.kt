package com.tokopedia.content.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 17/03/23
 */
class UiEventManager<T: UiEvent> @Inject constructor() {
    private val mutex = Mutex()

    private val _events = MutableStateFlow(emptyList<T>())

    /**
     * A flow emitting the current message to display.
     */
    val event: Flow<T?> = _events.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitEvent(message: T) {
        mutex.withLock {
            _events.value = _events.value + message
        }
    }

    suspend fun clearEvent(id: Long) {
        mutex.withLock {
            _events.value = _events.value.filterNot { it.id == id }
        }
    }
}

interface UiEvent {
    val id: Long
}
