package com.tokopedia.officialstore

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OfficialStoreDispatcherProviderImpl : OfficialStoreDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}