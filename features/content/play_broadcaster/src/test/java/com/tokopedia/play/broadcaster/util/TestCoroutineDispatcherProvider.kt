package com.tokopedia.play.broadcaster.util

import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jegul on 24/09/20
 */
class TestCoroutineDispatcherProvider(
        private val testDispatcher: CoroutineDispatcher
) : CoroutineDispatcherProvider {

    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = testDispatcher
    override val computation: CoroutineDispatcher
        get() = testDispatcher
}