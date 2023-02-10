package com.tokopedia.search

import com.tokopedia.search.result.complete
import com.tokopedia.search.result.error
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import rx.Subscriber

internal fun <T> UseCase<T>.stubExecute(
    stubData: T,
    requestParamsSlot: CapturingSlot<RequestParams> = slot(),
) {
    everyExecute(requestParamsSlot) answers {
        secondArg<Subscriber<T>>().complete(stubData)
    }
}

internal fun <T, E: Exception> UseCase<T>.stubExecuteFail(
    exception: E,
    requestParamsSlot: CapturingSlot<RequestParams> = slot(),
) {
    everyExecute(requestParamsSlot) answers {
        secondArg<Subscriber<T>>().error(exception)
    }
}

private fun <T> UseCase<T>.everyExecute(requestParamsSlot: CapturingSlot<RequestParams>) =
    every { execute(capture(requestParamsSlot), any()) }
