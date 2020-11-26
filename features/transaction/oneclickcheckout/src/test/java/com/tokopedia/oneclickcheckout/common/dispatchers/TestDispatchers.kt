package com.tokopedia.oneclickcheckout.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
object TestDispatchers: ExecutorDispatchers {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    override val main: CoroutineDispatcher
        get() = testCoroutineDispatcher

    override val io: CoroutineDispatcher
        get() = testCoroutineDispatcher

    override val default: CoroutineDispatcher
        get() = testCoroutineDispatcher
}