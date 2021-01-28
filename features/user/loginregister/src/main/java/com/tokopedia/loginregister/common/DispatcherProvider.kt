package com.tokopedia.loginregister.common

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Ade Fulki on 02/06/20.
 */

interface DispatcherProvider {
    fun ui(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}