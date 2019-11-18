package com.tokopedia.search.result

import com.tokopedia.search.result.common.UseCase
import io.mockk.MockKStubScope
import io.mockk.coEvery
import io.mockk.coVerify

internal fun UseCase<*>.stubExecute(): MockKStubScope<Any?, Any?> {
    val it = this

    return coEvery { it.execute(any()) }
}

internal fun UseCase<*>.isNeverExecuted() {
    return this.isExecuted(0)
}

internal fun UseCase<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.execute(any()) }
}