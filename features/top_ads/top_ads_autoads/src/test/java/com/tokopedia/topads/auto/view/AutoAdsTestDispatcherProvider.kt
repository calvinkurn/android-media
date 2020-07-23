package com.tokopedia.topads.auto.view

import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AutoAdsTestDispatcherProvider: AutoAdsDispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}