package com.tokopedia.play.helper

import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Created by jegul on 13/02/20
 */
object TestCoroutineDispatchersProvider : CoroutineDispatcherProvider {

    override val main: CoroutineDispatcher = TestCoroutineDispatcher()
    override val immediate: CoroutineDispatcher = TestCoroutineDispatcher()
    override val io: CoroutineDispatcher = TestCoroutineDispatcher()
}