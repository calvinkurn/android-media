package com.tokopedia.play.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jegul on 16/12/19
 */
interface CoroutineDispatcherProvider {

    val main: CoroutineDispatcher

    val immediate: CoroutineDispatcher

    val io: CoroutineDispatcher
}