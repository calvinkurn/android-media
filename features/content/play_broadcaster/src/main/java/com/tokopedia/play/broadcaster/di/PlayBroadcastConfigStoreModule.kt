package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.data.config.BroadcastScheduleConfigStore
import com.tokopedia.play.broadcaster.data.config.BroadcastScheduleConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.ProductConfigStore
import com.tokopedia.play.broadcaster.data.config.ProductConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.TitleConfigStore
import com.tokopedia.play.broadcaster.data.config.TitleConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.AccountConfigStore
import com.tokopedia.play.broadcaster.data.config.AccountConfigStoreImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PlayBroadcastConfigStoreModule {

    /**
     * Config
     */
    @Binds
    @ActivityRetainedScope
    abstract fun bindChannelConfigStore(configStore: ChannelConfigStoreImpl): ChannelConfigStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindProductConfigStore(configStore: ProductConfigStoreImpl): ProductConfigStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindTitleConfigStore(configStore: TitleConfigStoreImpl): TitleConfigStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindBroadcastScheduleConfigStore(configStore: BroadcastScheduleConfigStoreImpl): BroadcastScheduleConfigStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindBroadcastUGCConfigStore(configStore: AccountConfigStoreImpl): AccountConfigStore

    @Binds
    @ActivityRetainedScope
    abstract fun bindHydraConfigStore(configStore: HydraConfigStoreImpl): HydraConfigStore
}