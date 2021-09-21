package com.tokopedia.autocomplete

import rx.Subscriber
import java.lang.Exception

fun <T> Subscriber<T>.complete(emittedValue: T?) {
    onStart()
    onNext(emittedValue)
    onCompleted()
}

fun <T> Subscriber<T>.error(exception: Exception?) {
    onStart()
    onError(exception)
}