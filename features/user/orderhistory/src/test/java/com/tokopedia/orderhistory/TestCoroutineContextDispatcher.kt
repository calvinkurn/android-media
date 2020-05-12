package com.tokopedia.orderhistory

import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryCoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestCoroutineContextDispatcher: OrderHistoryCoroutineContextProvider() {
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
}