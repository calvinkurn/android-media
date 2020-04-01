package com.tokopedia.product.detail.view.util

import kotlinx.coroutines.CoroutineDispatcher

interface DynamicProductDetailDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}