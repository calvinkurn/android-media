package com.tokopedia.review.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import junit.framework.Assert
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

fun LiveData<*>.verifyReviewSuccessEquals(expected: Success<*>) {
    val expectedResult = expected.data
    val actualResult = (value as Success<*>).data
    Assert.assertEquals(expectedResult, actualResult)
}

fun LiveData<*>.verifyReviewErrorEquals(expected: Fail<*>) {
    val expectedResult = expected.fail::class.java
    val actualResult = (value as Fail<*>).fail::class.java
    Assert.assertEquals(expectedResult, actualResult)
}

fun LiveData<*>.verifyCoroutineSuccessEquals(expected: CoroutineSuccess<*>) {
    val expectedResult = expected.data
    val actualResult = (value as CoroutineSuccess<*>).data
    Assert.assertEquals(expectedResult, actualResult)
}

fun LiveData<*>.verifyCoroutineFailEquals(expected: CoroutineFail) {
    val expectedResult = expected.throwable::class.java
    val actualResult = (value as CoroutineFail).throwable::class.java
    Assert.assertEquals(expectedResult, actualResult)
}

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