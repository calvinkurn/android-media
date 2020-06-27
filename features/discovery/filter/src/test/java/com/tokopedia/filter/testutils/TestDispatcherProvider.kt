package com.tokopedia.filter.testutils

import com.tokopedia.discovery.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class TestDispatcherProvider: DispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
}