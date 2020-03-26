package com.tokopedia.hotel.common.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessica on 2020-01-09
 */

@Suppress("PropertyName")
open class HotelDispatcherProvider {
    open val io: CoroutineDispatcher = Dispatchers.IO
    open val ui: CoroutineDispatcher = Dispatchers.Default
}