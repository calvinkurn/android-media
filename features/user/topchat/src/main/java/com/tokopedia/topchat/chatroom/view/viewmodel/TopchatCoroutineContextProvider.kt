package com.tokopedia.topchat.chatroom.view.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class TopchatCoroutineContextProvider @Inject constructor() {
    open val Main: CoroutineContext = Dispatchers.Main
    open val IO: CoroutineContext = Dispatchers.IO

    val ioDispatcher: CoroutineDispatcher get() {
        return (IO as? CoroutineDispatcher) ?: Dispatchers.IO
    }
}