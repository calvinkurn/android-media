package com.tokopedia.unit.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher

object CoroutineTestDispatchers : CoroutineDispatchers {

    val coroutineDispatcher = TestCoroutineDispatcher()

    override val main = coroutineDispatcher

    override val io = coroutineDispatcher

    override val default = coroutineDispatcher

    override val immediate = coroutineDispatcher

    override val computation = coroutineDispatcher
}
