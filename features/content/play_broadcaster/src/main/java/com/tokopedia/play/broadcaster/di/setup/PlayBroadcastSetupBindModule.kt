package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.data.datastore.*
import dagger.Binds
import dagger.Module

/**
 * Created by jegul on 19/06/20
 */
@Module
abstract class PlayBroadcastSetupBindModule {

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindSetupDataStore(dataStore: PlayBroadcastSetupDataStoreImpl): PlayBroadcastSetupDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindDataStore(dataStore: PlayBroadcastDataStoreImpl): PlayBroadcastDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindProductDataStore(dataStore: ProductDataStoreImpl): ProductDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindCoverDataSource(dataStore: CoverDataStoreImpl): CoverDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindBroadcastScheduleDataSource(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore
}