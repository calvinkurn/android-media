package com.tokopedia.favorite.view

import kotlinx.coroutines.CoroutineDispatcher

interface FavoriteDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

}
