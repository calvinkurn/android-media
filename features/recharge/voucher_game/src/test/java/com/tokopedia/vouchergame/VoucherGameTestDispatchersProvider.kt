package com.tokopedia.vouchergame

import com.tokopedia.vouchergame.common.util.VoucherGameDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class VoucherGameTestDispatchersProvider : VoucherGameDispatchersProvider() {
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
}