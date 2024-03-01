package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManagerImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalyticImpl
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
import com.tokopedia.play.broadcaster.data.datastore.ProductTagDataStore
import com.tokopedia.play.broadcaster.data.datastore.ProductTagDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.TagsDataStore
import com.tokopedia.play.broadcaster.data.datastore.TagsDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.TitleDataStore
import com.tokopedia.play.broadcaster.data.datastore.TitleDataStoreImpl
import com.tokopedia.play.broadcaster.util.logger.error.BroadcasterErrorLogger
import com.tokopedia.play.broadcaster.util.logger.error.BroadcasterErrorLoggerImpl
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

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindAssetManager(assetManager: AssetManagerImpl): AssetManager

    @Binds
    @PlayBroadcastSetupScope
    abstract fun bindBroadcasterErrorLogger(logger: BroadcasterErrorLoggerImpl): BroadcasterErrorLogger
}
