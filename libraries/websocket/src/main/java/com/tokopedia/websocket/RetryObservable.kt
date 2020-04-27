package com.tokopedia.websocket

import java.util.concurrent.TimeUnit

import rx.Observable
import rx.functions.Func1


/**
 * Created by Steven.
 */
class RetryObservable internal constructor(private var maxRetries: Int, private val delay: Long) : Func1<Observable<out Throwable>, Observable<*>> {

    private var retryCount: Int = 0

    override fun call(attempts: Observable<out Throwable>?): Observable<*>? {
        return attempts?.let {
            it.flatMap { t ->
                if (++retryCount < maxRetries) {
                    Observable.timer(delay * retryCount, TimeUnit.MILLISECONDS)
                } else {
                    Observable.error(t)
                }
            }
        }
    }

    fun resetMaxRetries() {
        this.maxRetries = 0
    }
}
