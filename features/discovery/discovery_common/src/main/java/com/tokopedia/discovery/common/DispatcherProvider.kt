package com.tokopedia.discovery.common

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}