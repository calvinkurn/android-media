package com.tokopedia.shop_showcase.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestCoroutineDispatchers: CoroutineDispatcherProvider {

    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun default(): CoroutineDispatcher = Dispatchers.Unconfined
}