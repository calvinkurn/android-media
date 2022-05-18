package com.tokopedia.inbox.fake.common

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object CoroutineAndroidTestDispatchersProvider: CoroutineDispatchers {

    override val main = Dispatchers.Main

    override val io = Dispatchers.Main

    override val default = Dispatchers.Main

    override val immediate = Dispatchers.Main

    override val computation = Dispatchers.Main
}