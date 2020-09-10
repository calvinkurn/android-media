package com.tokopedia.digital.home.presentation.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("PropertyName")
open class RechargeHomepageDispatchersProvider {
    open val Main: CoroutineDispatcher = Dispatchers.Main
    open val IO: CoroutineDispatcher = Dispatchers.IO
}