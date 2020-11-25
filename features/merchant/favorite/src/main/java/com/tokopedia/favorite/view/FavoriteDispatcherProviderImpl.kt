package com.tokopedia.favorite.view

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FavoriteDispatcherProviderImpl: FavoriteDispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}
