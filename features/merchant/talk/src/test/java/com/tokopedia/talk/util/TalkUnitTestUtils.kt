package com.tokopedia.talk.util

import androidx.lifecycle.LiveData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import junit.framework.Assert

fun LiveData<*>.verifyValueEquals(expected: Any) {
    val actual = value
    Assert.assertEquals(expected, actual)
}

fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
    val expectedResult = expected.data
    val actualResult = (value as Success<*>).data
    Assert.assertEquals(expectedResult, actualResult)
}

fun LiveData<*>.verifyErrorEquals(expected: Fail) {
    val expectedResult = expected.throwable::class.java
    val actualResult = (value as Fail).throwable::class.java
    Assert.assertEquals(expectedResult, actualResult)
}