package com.tokopedia.settingnotif.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.coEvery
import org.assertj.core.api.Assertions
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

inline fun <reified T> SettingRepository.stubRepository(
        expectedValue: Any,
        onError: Map<Type, List<GraphqlError>>?
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    return stubResponseRepository(
            mapOf(T::class.java to expectedValue),
            onError
    )
}

fun SettingRepository.stubResponseRepository(
        onResult: Map<Type, Any>,
        onError: Map<Type, List<GraphqlError>>?
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    val it = this
    return coEvery {
        it.getResponse(any(), any())
    } answers {
        GraphqlResponse(
                onResult,
                onError,
                false
        )
    }
}

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

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

infix fun LiveData<*>.isEqualsTo(any: Any) {
    Assertions.assertThat(this.getOrAwaitValue()).isEqualTo(any)
}