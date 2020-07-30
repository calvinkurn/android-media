package com.tokopedia.common.travel.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 04/02/2020
 */
interface TravelDispatcherProvider {
    fun io() : CoroutineDispatcher
    fun ui() : CoroutineDispatcher
}