package com.tokopedia.homenav.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface NavDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}