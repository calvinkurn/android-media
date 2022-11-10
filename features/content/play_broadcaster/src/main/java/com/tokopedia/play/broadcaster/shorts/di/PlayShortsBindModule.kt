package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalytic
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalyticImpl
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.repository.*
import com.tokopedia.play.broadcaster.domain.repository.*
import com.tokopedia.play.broadcaster.shorts.data.PlayShortsRepositoryImpl
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsUiMapper
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
abstract class PlayShortsBindModule {

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsRepository(repository: PlayShortsRepositoryImpl): PlayShortsRepository

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsUiMapper(mapper: PlayShortsUiMapper): PlayShortsMapper

    @Binds
    @PlayShortsScope
    abstract fun bindPermissionSharedPrefs(sharedPref: HydraSharedPreferences): PermissionSharedPreferences

    /** Broadcaster Repository */
    @Binds
    @PlayShortsScope
    abstract fun bindChannelRepository(
        repo: PlayBroadcastChannelRepositoryImpl
    ): PlayBroadcastChannelRepository

    @Binds
    @PlayShortsScope
    abstract fun bindPinnedMessageRepository(
        repo: PlayBroadcastPinnedMessageRepositoryImpl
    ): PlayBroadcastPinnedMessageRepository

    @Binds
    @PlayShortsScope
    abstract fun bindInteractiveRepository(
        repo: PlayBroadcastInteractiveRepositoryImpl
    ): PlayBroadcastInteractiveRepository

    @Binds
    @PlayShortsScope
    abstract fun bindProductRepository(
        repo: PlayBroProductRepositoryImpl
    ): PlayBroProductRepository

    @Binds
    @PlayShortsScope
    abstract fun bindRepository(
        repo: PlayBroadcastRepositoryImpl
    ): PlayBroadcastRepository

    /** Play Broadcaster Util */
    @Binds
    @PlayShortsScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): PlayBroadcastDialogCustomizer

    /** Play Broadcaster Analytic */
    @Binds
    @PlayShortsScope
    abstract fun bindInteractiveAnalytic(interactiveAnalytic: PlayBroadcastInteractiveAnalyticImpl): PlayBroadcastInteractiveAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindSetupMenuAnalytic(setupMenuAnalytic: PlayBroSetupMenuAnalyticImpl): PlayBroSetupMenuAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindSetupTitleAnalytic(setupTitleAnalytic: PlayBroSetupTitleAnalyticImpl): PlayBroSetupTitleAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindSetupCoverAnalytic(setupCoverAnalytic: PlayBroSetupCoverAnalyticImpl): PlayBroSetupCoverAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindSetupProductAnalytic(setupProductAnalytic: PlayBroSetupProductAnalyticImpl): PlayBroSetupProductAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindSummaryAnalytic(summaryAnalytic: PlayBroadcastSummaryAnalyticImpl): PlayBroadcastSummaryAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindScheduleAnalytic(scheduleAnalytic: PlayBroScheduleAnalyticImpl): PlayBroScheduleAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPinProductAnalytic(pinProductAnalytic: PlayBroadcastPinProductAnalyticImpl): PlayBroadcastPinProductAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPinUGCAnalytic(ugcAnalytic: PlayBroadcastAccountAnalyticImpl): PlayBroadcastAccountAnalytic

    /** Play Broadcaster Config Store */
    @Binds
    @PlayShortsScope
    abstract fun bindChannelConfigStore(configStore: ChannelConfigStoreImpl): ChannelConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindProductConfigStore(configStore: ProductConfigStoreImpl): ProductConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindTitleConfigStore(configStore: TitleConfigStoreImpl): TitleConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastScheduleConfigStore(configStore: BroadcastScheduleConfigStoreImpl): BroadcastScheduleConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastUGCConfigStore(configStore: AccountConfigStoreImpl): AccountConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindHydraConfigStore(configStore: HydraConfigStoreImpl): HydraConfigStore
}
