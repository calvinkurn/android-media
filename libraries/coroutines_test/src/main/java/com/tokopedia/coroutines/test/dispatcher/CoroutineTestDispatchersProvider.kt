package com.tokopedia.coroutines.test.dispatcher

import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object CoroutineTestDispatchersProvider: CoroutineDispatchers {

    override val main = Dispatchers.Unconfined

    override val io = Dispatchers.Unconfined

    override val default = Dispatchers.Unconfined
}