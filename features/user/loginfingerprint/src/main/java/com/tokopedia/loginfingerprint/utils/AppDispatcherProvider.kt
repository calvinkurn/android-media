package com.tokopedia.loginfingerprint.utils

import com.tokopedia.loginfingerprint.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppDispatcherProvider: DispatcherProvider {
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
    override fun io(): CoroutineDispatcher = Dispatchers.IO
}