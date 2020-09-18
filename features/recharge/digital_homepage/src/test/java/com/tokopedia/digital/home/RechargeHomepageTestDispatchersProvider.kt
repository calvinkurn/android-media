package com.tokopedia.digital.home

import com.tokopedia.digital.home.presentation.util.RechargeHomepageDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RechargeHomepageTestDispatchersProvider : RechargeHomepageDispatchersProvider() {
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
}