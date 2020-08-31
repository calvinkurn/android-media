package com.tokopedia.sellerorder.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 07/05/20.
 */
class SomProductionDispatcherProvider : SomDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Main
    override fun ui(): CoroutineDispatcher = Dispatchers.Default
}