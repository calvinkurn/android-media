package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.type.OverwriteMode
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class DataStoreViewModel @Inject constructor(
        private val mDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    fun setDataStore(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode> = emptyList()) {
        mDataStore.overwrite(dataStore, modeExclusion = modeExclusion)
    }

    fun getDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore
    }
}