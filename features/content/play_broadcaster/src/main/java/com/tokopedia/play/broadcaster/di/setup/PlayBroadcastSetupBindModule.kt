package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStoreImpl
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
}