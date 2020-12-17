package com.tokopedia.kyc_centralized.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DispatcherProvider {
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
}