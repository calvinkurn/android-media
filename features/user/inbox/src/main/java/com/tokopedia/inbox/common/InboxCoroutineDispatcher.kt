package com.tokopedia.inbox.common

import kotlinx.coroutines.CoroutineDispatcher

interface InboxCoroutineDispatcher {
    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
}