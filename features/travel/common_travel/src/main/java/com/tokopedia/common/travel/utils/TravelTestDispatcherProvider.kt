package com.tokopedia.common.travel.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 04/02/2020
 */
class TravelTestDispatcherProvider: TravelDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
}