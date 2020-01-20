package com.tokopedia.search.result.common

import androidx.lifecycle.Observer

open class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}