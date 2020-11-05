package com.tokopedia.shop.score.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestCoroutineDispatchers: CoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}