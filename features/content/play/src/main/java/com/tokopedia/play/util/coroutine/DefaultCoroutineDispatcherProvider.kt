package com.tokopedia.play.util.coroutine

import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by jegul on 16/12/19
 */
class DefaultCoroutineDispatcherProvider : CoroutineDispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
}