package com.tokopedia.people.util

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

internal class UnconfinedDispatcherProvider : CoroutineDispatchers {

    private val dispatcher = UnconfinedTestDispatcher()

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val immediate: CoroutineDispatcher = dispatcher
    override val computation: CoroutineDispatcher = dispatcher
}

internal class StandardDispatcherProvider : CoroutineDispatchers {

    private val dispatcher = StandardTestDispatcher()

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val immediate: CoroutineDispatcher = dispatcher
    override val computation: CoroutineDispatcher = dispatcher
}
