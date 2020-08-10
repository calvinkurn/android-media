package com.tokopedia.power_merchant.subscribe.verification

import androidx.lifecycle.LiveData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import junit.framework.TestCase.assertEquals

internal fun<T> LiveData<T>.verifyValueEquals(expected: Any?) {
    val actual = value
    assertEquals(expected, actual)
}

internal fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>?) {
    val expectedResult = expected?.data
    val actualResult = (value as? Success<T>)?.data
    assertEquals(expectedResult, actualResult)
}

internal fun<T: Any> LiveData<Result<T>>.verifyErrorEquals(expected: Fail?) {
    val expectedResult = expected?.let { it.throwable::class.java }
    val actualResult = (value as? Fail)?.let { it.throwable::class.java }
    assertEquals(expectedResult, actualResult)
}