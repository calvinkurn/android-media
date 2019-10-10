package com.tokopedia.search.result

import com.tokopedia.discovery.common.coroutines.Repository
import io.mockk.MockKStubScope
import io.mockk.coEvery
import io.mockk.coVerify

internal fun Repository<*>.stubGetResponse(): MockKStubScope<Any?, Any?> {
    val it = this
    return coEvery { it.getResponse(any()) }
}

internal fun Repository<*>.isNeverExecuted() {
    return this.isExecuted(0)
}

internal fun Repository<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.getResponse(any()) }
}