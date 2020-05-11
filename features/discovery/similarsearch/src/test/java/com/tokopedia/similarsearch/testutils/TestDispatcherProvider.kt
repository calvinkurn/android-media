package com.tokopedia.similarsearch.testutils

import com.tokopedia.discovery.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class TestDispatcherProvider: DispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}