package com.tokopedia.buyerorder

import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 06/05/20.
 */

class UohTestDispatcherProvider: BuyerDispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}