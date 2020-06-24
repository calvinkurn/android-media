package com.tokopedia.play.broadcaster.data.datastore

/**
 * Created by jegul on 22/06/20
 */
interface PlayBroadcastDataStore {

    fun setFromSetupStore(setupDataStore: PlayBroadcastSetupDataStore)

    fun getSetupDataStore(): PlayBroadcastSetupDataStore
}