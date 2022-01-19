package com.tokopedia.review.common.coroutine

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineAndroidTestDispatchersProvider : CoroutineDispatchers {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Main
    override val immediate: CoroutineDispatcher = Dispatchers.Main
    override val computation: CoroutineDispatcher = Dispatchers.Main
}