package com.tokopedia.content.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class UnconfinedDispatcherProvider : CoroutineDispatchers {

    private val dispatcher = UnconfinedTestDispatcher()

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val immediate: CoroutineDispatcher = dispatcher
    override val computation: CoroutineDispatcher = dispatcher
}
