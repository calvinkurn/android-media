package com.tokopedia.play.broadcaster.di.broadcast

import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalytic
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalyticImpl
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
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
    abstract fun bindTitleDataSource(dataStore: TitleDataStoreImpl): TitleDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindTagsDataSource(dataStore: TagsDataStoreImpl): TagsDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindBroadcastScheduleDataSource(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore

    @Binds
    @PlayBroadcastScope
    abstract fun bindInteractiveDataSource(dataStore: InteractiveDataStoreImpl): InteractiveDataStore

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
    abstract fun bindTitleConfigStore(configStore: TitleConfigStoreImpl): TitleConfigStore

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

    /**
     * Analytic
     */
    @Binds
    @PlayBroadcastScope
    abstract fun bindContentTaggingAnalytic(contentTaggingAnalytic: PlayBroadcastContentTaggingAnalyticImpl): PlayBroadcastContentTaggingAnalytic

    @Binds
    @PlayBroadcastScope
    abstract fun bindInteractiveAnalytic(interactiveAnalytic: PlayBroadcastInteractiveAnalyticImpl): PlayBroadcastInteractiveAnalytic


    @PlayBroadcastScope
    @Binds
    abstract fun bindLogger(logger: PlayLoggerImpl): PlayLogger
}
