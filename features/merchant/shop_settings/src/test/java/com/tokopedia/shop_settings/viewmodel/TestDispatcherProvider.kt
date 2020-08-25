package com.tokopedia.shop_settings.viewmodel

import com.tokopedia.shop.settings.common.coroutine.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : CoroutineDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined
    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined

}