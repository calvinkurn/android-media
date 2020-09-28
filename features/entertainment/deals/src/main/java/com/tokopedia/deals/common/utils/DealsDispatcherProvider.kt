package com.tokopedia.deals.common.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by jessica on 13/07/20
 */

interface DealsDispatcherProvider {
    fun io() : CoroutineDispatcher
    fun ui() : CoroutineDispatcher
}