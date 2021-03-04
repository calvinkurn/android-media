package com.tokopedia.seller.action.coroutine

import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object SellerActionTestCoroutineDispatchers: SellerActionDispatcherProvider {

    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined

}