package com.tokopedia.abstraction.common.dispatcher

import kotlinx.coroutines.Dispatchers

object CoroutineDispatchersProvider: CoroutineDispatchers {

    override val main = Dispatchers.Main

    override val io = Dispatchers.IO

    override val default = Dispatchers.Default
}