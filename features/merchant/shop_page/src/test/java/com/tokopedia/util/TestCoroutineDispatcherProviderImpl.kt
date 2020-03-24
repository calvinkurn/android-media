package com.tokopedia.util

import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestCoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Unconfined

    override fun io() = Dispatchers.Unconfined

    override fun default() = Dispatchers.Unconfined
}