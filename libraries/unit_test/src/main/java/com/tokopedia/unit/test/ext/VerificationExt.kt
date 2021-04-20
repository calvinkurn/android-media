package com.tokopedia.unit.test.ext

import androidx.lifecycle.LiveData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import org.junit.Assert.assertEquals

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