package com.tokopedia.content.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class StandardDispatcherProvider : CoroutineDispatchers {

    private val dispatcher = StandardTestDispatcher()

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val immediate: CoroutineDispatcher = dispatcher
    override val computation: CoroutineDispatcher = dispatcher
}
