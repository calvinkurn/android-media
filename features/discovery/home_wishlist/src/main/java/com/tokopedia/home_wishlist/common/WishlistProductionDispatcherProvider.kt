package com.tokopedia.home_wishlist.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class WishlistProductionDispatcherProvider: WishlistDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}