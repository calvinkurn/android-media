package com.tokopedia.managename.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DispatcherProvider {
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
}