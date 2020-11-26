package com.tokopedia.deals.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessica on 13/07/20
 */

class DealsTestDispatcherProvider: DealsDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
}