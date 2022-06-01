package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStore
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStore
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.InteractiveDataStore
import com.tokopedia.play.broadcaster.data.datastore.InteractiveDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.TagsDataStore
import com.tokopedia.play.broadcaster.data.datastore.TagsDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.TitleDataStore
import com.tokopedia.play.broadcaster.data.datastore.TitleDataStoreImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PlayBroadcastDataStoreModule {

    @Binds
    @ActivityRetainedScope
    abstract fun bindSetupDataStore(dataStore: PlayBroadcastSetupDataStoreImpl): PlayBroadcastSetupDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindDataStore(dataStore: PlayBroadcastDataStoreImpl): PlayBroadcastDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindCoverDataStore(dataStore: CoverDataStoreImpl): CoverDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindTitleDataStore(dataStore: TitleDataStoreImpl): TitleDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindTagsDataStore(dataStore: TagsDataStoreImpl): TagsDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindBroadcastScheduleDataStore(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindInteractiveDataStore(dataStore: InteractiveDataStoreImpl): InteractiveDataStore
}