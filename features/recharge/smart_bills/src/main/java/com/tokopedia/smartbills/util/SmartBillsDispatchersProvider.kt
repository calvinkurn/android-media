package com.tokopedia.smartbills.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("PropertyName")
open class SmartBillsDispatchersProvider {
    open val Main: CoroutineDispatcher = Dispatchers.Main
    open val IO: CoroutineDispatcher = Dispatchers.IO
}