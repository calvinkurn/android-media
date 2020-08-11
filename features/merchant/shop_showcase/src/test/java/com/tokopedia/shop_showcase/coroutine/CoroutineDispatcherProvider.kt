package com.tokopedia.shop_showcase.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}