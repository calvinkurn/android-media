package com.tokopedia.ordermanagement.snapshot.util

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 1/25/21.
 */
interface SnapshotDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}