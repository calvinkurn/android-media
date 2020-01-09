package com.tokopedia.hotel

import com.tokopedia.hotel.common.util.HotelDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessica on 2020-01-09
 */

class HotelDispatchersProviderTest : HotelDispatcherProvider() {
    override val IO: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val Main: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}