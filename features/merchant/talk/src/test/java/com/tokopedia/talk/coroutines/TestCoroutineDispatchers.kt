package com.tokopedia.talk.coroutines

import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestCoroutineDispatchers: CoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}