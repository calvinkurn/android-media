package com.tokopedia.liveness.util

import com.tokopedia.liveness.utils.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object CoroutineTestDispatchersProvider: CoroutineDispatchers {
    override val io = Dispatchers.Unconfined
    override val main = Dispatchers.Unconfined
    override val default = Dispatchers.Unconfined
}