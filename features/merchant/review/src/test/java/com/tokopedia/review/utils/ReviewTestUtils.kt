package com.tokopedia.review.utils

import androidx.lifecycle.LiveData
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
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

fun LiveData<*>.verifyErrorEquals(expected: Fail<*>) {
    val expectedResult = expected.fail::class.java
    val actualResult = (value as Fail<*>).fail::class.java
    Assert.assertEquals(expectedResult, actualResult)
}