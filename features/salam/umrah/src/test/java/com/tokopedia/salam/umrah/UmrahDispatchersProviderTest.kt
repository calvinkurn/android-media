package com.tokopedia.salam.umrah

import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UmrahDispatchersProviderTest : UmrahDispatchersProvider() {
    override val IO: CoroutineDispatcher
    get() = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher
    get() = Dispatchers.Unconfined
}