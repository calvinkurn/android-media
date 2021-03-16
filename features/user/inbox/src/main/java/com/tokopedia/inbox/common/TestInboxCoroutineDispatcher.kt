package com.tokopedia.inbox.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class TestInboxCoroutineDispatcher: InboxCoroutineDispatcher {
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
}