package com.tokopedia.shop_settings.common.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import junit.framework.TestCase
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataUtil {

    internal fun <T> LiveData<T>.observeAwaitValue(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }

    internal fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>?) {
        val expectedResult = expected?.data
        val actualResult = (value as? Success<T>)?.data
        TestCase.assertEquals(expectedResult, actualResult)
    }
}