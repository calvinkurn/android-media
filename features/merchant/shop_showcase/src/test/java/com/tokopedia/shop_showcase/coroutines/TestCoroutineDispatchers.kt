package com.tokopedia.shop_showcase.coroutines

import com.tokopedia.shop_showcase.common.ShopShowcaseDispatchProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchers : ShopShowcaseDispatchProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

}