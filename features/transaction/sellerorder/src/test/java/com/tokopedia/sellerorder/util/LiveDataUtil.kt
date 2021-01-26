package com.tokopedia.sellerorder.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.observeAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }
    observeForever(observer)
    latch.await(time, timeUnit)
    return value
}