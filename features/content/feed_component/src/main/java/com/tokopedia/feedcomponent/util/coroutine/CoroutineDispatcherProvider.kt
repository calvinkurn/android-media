package com.tokopedia.feedcomponent.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jegul on 2019-11-05
 */
interface CoroutineDispatcherProvider {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher

    val default: CoroutineDispatcher
}