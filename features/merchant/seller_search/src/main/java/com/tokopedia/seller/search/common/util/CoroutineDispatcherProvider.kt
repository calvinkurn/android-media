package com.tokopedia.seller.search.common.util

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}