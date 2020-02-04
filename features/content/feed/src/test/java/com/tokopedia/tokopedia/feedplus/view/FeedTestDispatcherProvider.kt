package com.tokopedia.tokopedia.feedplus.view

import com.tokopedia.feedplus.view.di.FeedDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FeedTestDispatcherProvider: FeedDispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}