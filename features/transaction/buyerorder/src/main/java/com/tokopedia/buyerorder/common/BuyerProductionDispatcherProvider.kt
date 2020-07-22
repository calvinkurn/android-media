package com.tokopedia.buyerorder.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerProductionDispatcherProvider: BuyerDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Main
    override fun ui(): CoroutineDispatcher = Dispatchers.Default
}

