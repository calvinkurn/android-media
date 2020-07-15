package com.tokopedia.loginfingerprint.utils

import com.tokopedia.loginfingerprint.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DispatcherProvider {
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
}