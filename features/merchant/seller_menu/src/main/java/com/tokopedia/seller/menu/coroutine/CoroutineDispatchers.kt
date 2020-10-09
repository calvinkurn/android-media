package com.tokopedia.seller.menu.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}