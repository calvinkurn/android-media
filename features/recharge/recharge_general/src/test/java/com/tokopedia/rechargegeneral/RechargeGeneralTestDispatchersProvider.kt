package com.tokopedia.rechargegeneral

import com.tokopedia.rechargegeneral.util.RechargeGeneralDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RechargeGeneralTestDispatchersProvider : RechargeGeneralDispatchersProvider() {
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
}