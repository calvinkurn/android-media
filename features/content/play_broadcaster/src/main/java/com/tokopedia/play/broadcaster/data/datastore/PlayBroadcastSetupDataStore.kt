package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.data.type.OverwriteMode

interface PlayBroadcastSetupDataStore : ProductDataStore, CoverDataStore {

    fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode> = emptyList())

    fun getProductDataStore(): ProductDataStore

    fun getCoverDataStore(): CoverDataStore
}