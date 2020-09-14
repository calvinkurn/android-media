package com.tokopedia.vouchercreation.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}