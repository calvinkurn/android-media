package com.tokopedia.product.manage.feature.stockreminder.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher


interface CoroutineDispatcherProvider {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher

    val default: CoroutineDispatcher
}