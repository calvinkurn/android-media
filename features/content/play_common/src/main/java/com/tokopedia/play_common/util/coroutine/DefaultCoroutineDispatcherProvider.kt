package com.tokopedia.play_common.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by jegul on 22/09/20
 */
class DefaultCoroutineDispatcherProvider : CoroutineDispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val computation: CoroutineDispatcher
        get() = Dispatchers.Default
}