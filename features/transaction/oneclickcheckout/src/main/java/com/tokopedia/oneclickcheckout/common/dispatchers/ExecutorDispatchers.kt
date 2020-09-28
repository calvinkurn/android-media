package com.tokopedia.oneclickcheckout.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface ExecutorDispatchers {

    val main: CoroutineDispatcher

    val io: CoroutineDispatcher

    val default: CoroutineDispatcher
}