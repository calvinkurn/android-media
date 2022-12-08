package com.tokopedia.unit.test.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import org.junit.Assert.assertEquals

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun<T> LiveData<T>.verifyValueEquals(expected: Any?) {
    assertEquals(expected, value)
}

fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>) {
    val expectedResult = expected.data
    val actualResult = (value as? Success<T>)?.data
    assertEquals(expectedResult, actualResult)
}

fun<T: Any> LiveData<Result<T>>.verifyErrorEquals(expected: Fail) {
    val expectedResult = expected.throwable::class.java
    val actualResult = (value as? Fail)?.let {
        it.throwable::class.java
    }
    assertEquals(expectedResult, actualResult)
}
