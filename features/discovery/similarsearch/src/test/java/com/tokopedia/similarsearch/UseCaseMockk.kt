package com.tokopedia.similarsearch

import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.MockKStubScope

internal fun UseCase<*>.stubExecute(): MockKStubScope<Any?, Any?> {
    val it = this

    return io.mockk.coEvery { it.executeOnBackground() }
}

internal fun UseCase<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    io.mockk.coVerify(exactly = executionCount) { it.executeOnBackground() }
}