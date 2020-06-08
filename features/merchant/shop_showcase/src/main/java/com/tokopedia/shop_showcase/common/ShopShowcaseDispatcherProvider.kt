package com.tokopedia.shop_showcase.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ShopShowcaseDispatcherProvider: ShopShowcaseDispatchProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}