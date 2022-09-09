package com.tokopedia.play_common.eventbus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class EventBus<T: Any>(
    bufferCapacity: Int = 50,
) {

    private val _bus = MutableSharedFlow<T>(extraBufferCapacity = bufferCapacity)

    @Suppress("UNCHECKED_CAST")
    fun <Topic : T> subscribe(type: Class<Topic>): Flow<Topic> {
        return _bus.mapNotNull {
            if (type.isAssignableFrom(it::class.java)) it as Topic
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