package com.tokopedia.play.util.observer

import com.tokopedia.play_common.util.event.Event

/**
 * Created by jegul on 11/05/20
 */
class DistinctEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : DistinctObserver<Event<T>>({ event ->
    event.getContentIfNotHandled()?.let { value ->
        onEventUnhandledContent(value)
    }
})