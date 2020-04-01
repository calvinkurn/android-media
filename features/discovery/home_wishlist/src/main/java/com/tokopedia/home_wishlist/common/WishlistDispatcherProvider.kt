package com.tokopedia.home_wishlist.common

import kotlinx.coroutines.CoroutineDispatcher

interface WishlistDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}