package com.tokopedia.buyerorder.common

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 12/06/20.
 */
interface BuyerDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}