package com.tokopedia.shop.open.shop_open_revamp.common

import kotlinx.coroutines.CoroutineDispatcher

interface ShopOpenDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}