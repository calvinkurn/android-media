package com.tokopedia.flashsale.management

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class UnitTestKotlin {

    @Test
    fun checekObser(): Unit = runBlocking {
        val mock = MockObservable()
        val job = launch {
            val value = async { mock.executeSync() }
            println("value ${value.await()}")
        }
        delay(1000)
        job.cancel()
        println("job is cancelled ${Thread.currentThread().name}")
        assert(true)
    }

    @Test
    fun checkCoroutine(): Unit = runBlocking {
        val mock = MockObservable()
        val job = launch {
            val value = async { mock.coroutinesDelay()}
            println("value ${value.await()}")
        }
        delay(1000)
        job.cancel()
        println("job is cancelled ${Thread.currentThread().name}")
        assert(true)
    }
}