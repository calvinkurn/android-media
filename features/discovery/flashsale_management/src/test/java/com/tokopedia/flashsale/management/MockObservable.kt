package com.tokopedia.flashsale.management

import kotlinx.coroutines.experimental.delay
import rx.Observable
import java.util.concurrent.TimeUnit

class MockObservable {

    fun createObservabel() = Observable.just("Hello World")
            .delay(5, TimeUnit.SECONDS)
            .doOnNext { println("from thread ${Thread.currentThread().name}") }
            .doOnTerminate {
                println("Observable Terminated")
            }.doOnError {
                println("Error: ${it.printStackTrace()}")
            }

    fun executeSync(): String = createObservabel().defaultIfEmpty("").toBlocking().first()


    suspend fun coroutinesDelay(): String{
        delay(5000)
        println("from thread ${Thread.currentThread().name}")
        return "Hello World"
    }
}