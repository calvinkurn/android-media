package com.tokopedia.seller.menu.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
object TestCoroutineDispatchers: CoroutineDispatchers {

    val testDispatcher = TestCoroutineDispatcher()

    override val io: CoroutineDispatcher
        get() = testDispatcher

    override val main: CoroutineDispatcher
        get() = testDispatcher
}