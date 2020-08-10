package com.tokopedia.favorite.view

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

}