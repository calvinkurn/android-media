package com.tokopedia.home.beranda.common

import kotlinx.coroutines.CoroutineDispatcher

interface HomeDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}