package com.tokopedia.shop_showcase.common

import kotlinx.coroutines.CoroutineDispatcher

interface ShopShowcaseDispatchProvider {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
}