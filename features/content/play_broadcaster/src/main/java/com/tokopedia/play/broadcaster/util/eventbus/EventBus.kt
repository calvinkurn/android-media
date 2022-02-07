package com.tokopedia.play.broadcaster.util.eventbus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Created by kenny.hadisaputra on 02/02/22
 */
class EventBus<T: Any>(
    bufferCapacity: Int = 50,
) {

    private val _bus = MutableSharedFlow<T>(extraBufferCapacity = bufferCapacity)

    @Suppress("UNCHECKED_CAST")
    fun <Topic : T> subscribe(type: Class<Topic>): Flow<Topic> {
        return _bus.mapNotNull {
            if (it::class.java == type) it as Topic
            else null
        }
    }

    fun subscribe(): Flow<T> {
        return _bus
    }

    fun emit(event: T) {
        _bus.tryEmit(event)
    }
}