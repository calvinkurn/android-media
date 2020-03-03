package com.tokopedia.product.util

import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DynamicProductDetailDispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}