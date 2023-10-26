package com.tokopedia.tokochat.config.util

import kotlinx.coroutines.CoroutineDispatcher

interface TokoChatCoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}
