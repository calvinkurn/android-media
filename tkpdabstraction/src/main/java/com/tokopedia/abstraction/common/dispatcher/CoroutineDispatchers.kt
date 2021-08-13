package com.tokopedia.abstraction.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val immediate: CoroutineDispatcher
    val computation: CoroutineDispatcher
}