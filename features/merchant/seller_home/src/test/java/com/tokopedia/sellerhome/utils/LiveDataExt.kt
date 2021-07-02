package com.tokopedia.sellerhome.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
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

fun<T: Any> LiveData<SettingResponseState<T>>.verifyStateSuccessEquals(expected: SettingResponseState.SettingSuccess<T>) {
    val expectedResult = expected.data
    val actualResult = (value as? SettingResponseState.SettingSuccess<T>)?.data
    Assert.assertEquals(expectedResult, actualResult)
}

fun<T: Any> LiveData<SettingResponseState<T>>.verifyStateErrorEquals(expected: SettingResponseState.SettingError) {
    val expectedResult = expected.throwable::class.java
    val actualResult = (value as? SettingResponseState.SettingError)?.let {
        it.throwable::class.java
    }
    Assert.assertEquals(expectedResult, actualResult)
}