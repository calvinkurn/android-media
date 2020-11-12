package com.tokopedia.coroutines.test.dispatcher

import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
object CoroutineTestDispatchers: CoroutineDispatchers {

    internal val coroutineDispatcher = TestCoroutineDispatcher()

    override val main = coroutineDispatcher

    override val io = coroutineDispatcher

    override val default = coroutineDispatcher
}