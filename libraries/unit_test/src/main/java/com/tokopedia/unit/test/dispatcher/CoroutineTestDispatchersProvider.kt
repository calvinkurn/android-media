package com.tokopedia.unit.test.dispatcher

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object CoroutineTestDispatchersProvider: CoroutineDispatchers {

    override val main = Dispatchers.Unconfined

    override val io = Dispatchers.Unconfined

    override val default = Dispatchers.Unconfined

    override val immediate = Dispatchers.Unconfined

    override val computation = Dispatchers.Unconfined
}