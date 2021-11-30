package com.tokopedia.review.utils

import androidx.lifecycle.LiveData
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import junit.framework.Assert
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