package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalyticImpl
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
    abstract fun bindCoverDataStore(dataStore: CoverDataStoreImpl): CoverDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindTitleDataStore(dataStore: TitleDataStoreImpl): TitleDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindTagsDataStore(dataStore: TagsDataStoreImpl): TagsDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindBroadcastScheduleDataStore(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindInteractiveDataStore(dataStore: InteractiveDataStoreImpl): InteractiveDataStore

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindPlayBroCoverPickerAnalytic(analytic: PlayBroCoverPickerAnalyticImpl): PlayBroCoverPickerAnalytic

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindProductTagDataStore(dataStore: ProductTagDataStoreImpl): ProductTagDataStore
}
