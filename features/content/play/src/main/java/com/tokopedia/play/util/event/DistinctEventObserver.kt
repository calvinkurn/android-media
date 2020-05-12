package com.tokopedia.play.util.event

import com.tokopedia.play.util.observer.DistinctObserver

/**
 * Created by jegul on 11/05/20
 */
class DistinctEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : DistinctObserver<Event<T>>({ event ->
    event?.getContentIfNotHandled()?.let { value ->
        onEventUnhandledContent(value)
    }
})