package com.tokopedia.play.broadcaster.di.broadcast

import com.tokopedia.play.broadcaster.data.datastore.*
import dagger.Binds
import dagger.Module

/**
 * Created by jegul on 22/06/20
 */
@Module
abstract class PlayBroadcastBindModule {

    @Binds
    @PlayBroadcastScope
    abstract fun bindSetupDataStore(dataStore: PlayBroadcastSetupDataStoreImpl): PlayBroadcastSetupDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindDataStore(dataStore: PlayBroadcastDataStoreImpl): PlayBroadcastDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindProductDataStore(dataStore: ProductDataStoreImpl): ProductDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindCoverDataSource(dataStore: CoverDataStoreImpl): CoverDataStore
}
