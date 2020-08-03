package com.tokopedia.play.broadcaster.data.datastore

import javax.inject.Inject

/**
 * Created by jegul on 22/06/20
 */
class PlayBroadcastDataStoreImpl @Inject constructor(
        private val mSetupDataStore: PlayBroadcastSetupDataStore
) : PlayBroadcastDataStore {

    override fun setFromSetupStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mSetupDataStore.overwrite(setupDataStore)
    }

    override fun getSetupDataStore(): PlayBroadcastSetupDataStore {
        return mSetupDataStore
    }
}