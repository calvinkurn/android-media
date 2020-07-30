package com.tokopedia.home.test.rules

import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: HomeDispatcherProvider, Lazy<HomeDispatcherProvider> {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun get(): HomeDispatcherProvider {
        return this
    }
}