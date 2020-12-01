package com.tokopedia.unit.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
object CoroutineTestDispatchers: CoroutineDispatchers {

    internal val coroutineDispatcher = TestCoroutineDispatcher()

    override val main = coroutineDispatcher

    override val io = coroutineDispatcher

    override val default = coroutineDispatcher
}