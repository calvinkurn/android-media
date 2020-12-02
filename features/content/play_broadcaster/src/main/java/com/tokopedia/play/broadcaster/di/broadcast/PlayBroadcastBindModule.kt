package com.tokopedia.play.broadcaster.di.broadcast

import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
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

    @Binds
    @PlayBroadcastScope
    abstract fun bindBroadcastScheduleDataSource(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore

    /**
     * Config
     */
    @Binds
    @PlayBroadcastScope
    abstract fun bindChannelConfigStore(configStore: ChannelConfigStoreImpl): ChannelConfigStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindProductConfigStore(configStore: ProductConfigStoreImpl): ProductConfigStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindCoverConfigStore(configStore: CoverConfigStoreImpl): CoverConfigStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindBroadcastScheduleConfigStore(configStore: BroadcastScheduleConfigStoreImpl): BroadcastScheduleConfigStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindHydraConfigStore(configStore: HydraConfigStoreImpl): HydraConfigStore

    /**
     * Pref
     */
    @Binds
    @PlayBroadcastScope
    abstract fun bindPermissionSharedPrefs(sharedPref: HydraSharedPreferences): PermissionSharedPreferences

    /**
     * Util
     */
    @Binds
    @PlayBroadcastScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): PlayBroadcastDialogCustomizer
}
