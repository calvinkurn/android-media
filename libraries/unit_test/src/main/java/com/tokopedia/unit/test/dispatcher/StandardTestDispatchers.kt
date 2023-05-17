package com.tokopedia.unit.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.test.StandardTestDispatcher

object StandardTestDispatchers : CoroutineDispatchers {

    val coroutineDispatcher = StandardTestDispatcher()

    override val main = coroutineDispatcher

    override val io = coroutineDispatcher

    override val default = coroutineDispatcher

    override val immediate = coroutineDispatcher

    override val computation = coroutineDispatcher
}
