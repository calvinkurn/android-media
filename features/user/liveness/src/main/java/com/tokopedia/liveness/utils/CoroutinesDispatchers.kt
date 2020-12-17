package com.tokopedia.liveness.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}