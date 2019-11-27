package com.tokopedia.discovery.common.coroutines

import com.tokopedia.discovery.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ProductionDispatcherProvider: DispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}