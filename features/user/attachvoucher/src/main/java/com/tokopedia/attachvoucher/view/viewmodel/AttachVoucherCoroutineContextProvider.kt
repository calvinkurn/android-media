package com.tokopedia.attachvoucher.view.viewmodel

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AttachVoucherCoroutineContextProvider @Inject constructor() {
    open val Main: CoroutineContext = Dispatchers.Main
    open val IO: CoroutineContext = Dispatchers.IO
}