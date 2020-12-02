package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class BroadcastScheduleDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider
): BroadcastScheduleDataStore {

}