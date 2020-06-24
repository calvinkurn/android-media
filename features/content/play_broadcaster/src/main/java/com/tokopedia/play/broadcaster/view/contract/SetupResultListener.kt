package com.tokopedia.play.broadcaster.view.contract

import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore

/**
 * Created by jegul on 22/06/20
 */
interface SetupResultListener {

    fun onSetupCanceled()
    fun onSetupCompletedWithData(dataStore: PlayBroadcastSetupDataStore)
}