package com.tokopedia.smartbills

import com.tokopedia.smartbills.util.SmartBillsDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SmartBillsTestDispatchersProvider : SmartBillsDispatchersProvider() {
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
}