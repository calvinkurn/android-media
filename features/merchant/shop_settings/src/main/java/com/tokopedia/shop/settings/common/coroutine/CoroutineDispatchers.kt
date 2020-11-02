package com.tokopedia.shop.settings.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}