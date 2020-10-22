package com.tokopedia.shop.open.common

import kotlinx.coroutines.CoroutineDispatcher

interface ShopOpenDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}