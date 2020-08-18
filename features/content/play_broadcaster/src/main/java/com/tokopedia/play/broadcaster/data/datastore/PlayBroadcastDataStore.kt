package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData

/**
 * Created by jegul on 22/06/20
 */
interface PlayBroadcastDataStore {

    fun setFromSetupStore(setupDataStore: PlayBroadcastSetupDataStore)

    fun getSetupDataStore(): PlayBroadcastSetupDataStore

    fun getSerializableData(): SerializableHydraSetupData

    fun setSerializableData(data: SerializableHydraSetupData)
}