package com.tokopedia.review.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import org.junit.Assert
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

const val JSON_KEY_DATA = "data"

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
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T? {
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

fun getJsonFromFile(path: String): String {
    val uri = ClassLoader.getSystemClassLoader().getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}

inline fun <reified T> parseJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}

inline fun <reified T : Any> createSuccessResponse(jsonPath: String): GraphqlResponse {
    val mockJsonResponse = getJsonFromFile(jsonPath)
    val response: JsonObject = parseJson(mockJsonResponse)
    val jsonData = response.get(JSON_KEY_DATA).toString()
    val data: T = parseJson(jsonData)
    val result = mapOf<Type, Any>(T::class.java to data)
    val error = mapOf<Type, List<GraphqlError>>(T::class.java to emptyList())
    return GraphqlResponse(result, error, false)
}

inline fun <reified T : Any> createErrorResponse(): GraphqlResponse {
    val errorResponse = GraphqlError().apply {
        message = "error message"
    }
    val result = mapOf<Type, Any?>(T::class.java to null)
    val error = mapOf<Type, List<GraphqlError>>(T::class.java to listOf(errorResponse))
    return GraphqlResponse(result, error, false)
}

inline fun <reified T : Any> assertInstanceOf(actual: Any?) {
    Assert.assertTrue(actual is T)
}