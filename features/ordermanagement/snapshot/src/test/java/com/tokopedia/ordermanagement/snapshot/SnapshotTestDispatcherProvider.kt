package com.tokopedia.ordermanagement.snapshot

import com.tokopedia.ordermanagement.snapshot.util.SnapshotDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 2/5/21.
 */
class SnapshotTestDispatcherProvider: SnapshotDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}