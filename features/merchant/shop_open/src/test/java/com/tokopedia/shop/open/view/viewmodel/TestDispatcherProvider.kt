package com.tokopedia.shop.open.view.viewmodel

import com.tokopedia.shop.open.common.ShopOpenDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: ShopOpenDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}