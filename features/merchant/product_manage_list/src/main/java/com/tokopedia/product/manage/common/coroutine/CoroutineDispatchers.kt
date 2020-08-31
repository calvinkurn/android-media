package com.tokopedia.product.manage.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}