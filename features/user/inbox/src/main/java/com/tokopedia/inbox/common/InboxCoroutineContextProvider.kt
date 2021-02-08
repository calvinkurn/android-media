package com.tokopedia.inbox.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class InboxCoroutineContextProvider @Inject constructor(): InboxCoroutineDispatcher {
    override val Main: CoroutineDispatcher = Dispatchers.Main
    override val IO: CoroutineDispatcher = Dispatchers.IO
}