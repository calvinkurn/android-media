package com.tokopedia.power_merchant.subscribe.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}