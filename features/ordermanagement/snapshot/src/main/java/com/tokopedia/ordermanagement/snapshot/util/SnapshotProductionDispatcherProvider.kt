package com.tokopedia.ordermanagement.snapshot.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 1/25/21.
 */
class SnapshotProductionDispatcherProvider: SnapshotDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
}