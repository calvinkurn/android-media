package com.tokopedia.reviewseller.feature.reviewlist.util

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}