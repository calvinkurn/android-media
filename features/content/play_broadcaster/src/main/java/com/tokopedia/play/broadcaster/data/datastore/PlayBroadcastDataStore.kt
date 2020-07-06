package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.data.model.HydraSetupData

/**
 * Created by jegul on 22/06/20
 */
interface PlayBroadcastDataStore {

    fun setFromSetupStore(setupDataStore: PlayBroadcastSetupDataStore)

    fun getSetupDataStore(): PlayBroadcastSetupDataStore

    fun getAllData(): HydraSetupData

    fun setAllData(data: HydraSetupData)
}