package com.tokopedia.play_common.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jegul on 22/09/20
 */
interface CoroutineDispatcherProvider {

    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val immediate: CoroutineDispatcher
    val computation: CoroutineDispatcher
}