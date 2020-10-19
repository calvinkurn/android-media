package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainNavDispatcherProviderImpl : NavDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}