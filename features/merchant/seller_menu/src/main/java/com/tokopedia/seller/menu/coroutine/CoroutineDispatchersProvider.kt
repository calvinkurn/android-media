package com.tokopedia.seller.menu.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineDispatchersProvider: CoroutineDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}