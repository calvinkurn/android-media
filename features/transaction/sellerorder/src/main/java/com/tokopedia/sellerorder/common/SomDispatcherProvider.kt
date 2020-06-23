package com.tokopedia.sellerorder.common

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 06/05/20.
 */

interface SomDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}