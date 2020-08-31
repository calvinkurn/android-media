package com.tokopedia.brandlist.common

import kotlinx.coroutines.CoroutineDispatcher

interface BrandlistDispatcherProvider {

    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

}