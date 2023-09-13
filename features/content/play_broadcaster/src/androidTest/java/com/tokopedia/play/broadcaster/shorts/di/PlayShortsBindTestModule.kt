package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPrefImpl
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.checker.AssetCheckerImpl
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManagerImpl
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalytic
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalytic
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.sender.PlayBroadcasterAnalyticSender
import com.tokopedia.play.broadcaster.analytic.sender.PlayBroadcasterAnalyticSenderImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalyticImpl
import com.tokopedia.play.broadcaster.data.config.AccountConfigStore
import com.tokopedia.play.broadcaster.data.config.AccountConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.BroadcastScheduleConfigStore
import com.tokopedia.play.broadcaster.data.config.BroadcastScheduleConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.BroadcastingConfigStore
import com.tokopedia.play.broadcaster.data.config.BroadcastingConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.ProductConfigStore
import com.tokopedia.play.broadcaster.data.config.ProductConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.TitleConfigStore
import com.tokopedia.play.broadcaster.data.config.TitleConfigStoreImpl
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalyticImpl
import com.tokopedia.play.broadcaster.shorts.analytic.affiliate.PlayShortsAffiliateAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.affiliate.PlayShortsAffiliateAnalyticImpl
import com.tokopedia.play.broadcaster.shorts.analytic.general.PlayShortsGeneralAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.general.PlayShortsGeneralAnalyticImpl
import com.tokopedia.play.broadcaster.shorts.analytic.product.PlayShortsProductPickerUGCAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.product.PlayShortsSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSenderImpl
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@Module
abstract class PlayShortsBindTestModule {

    @Binds
    @PlayShortsScope
    abstract fun bindPermissionSharedPrefs(sharedPref: HydraSharedPreferences): PermissionSharedPreferences

    @Binds
    @PlayShortsScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): PlayBroadcastDialogCustomizer

    /** Analytic */
    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsAnalytic(analytic: PlayShortsAnalyticImpl): PlayShortsAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsGeneralAnalytic(analytic: PlayShortsGeneralAnalyticImpl): PlayShortsGeneralAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsAffiliateAnalytic(analytic: PlayShortsAffiliateAnalyticImpl): PlayShortsAffiliateAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsProductPickerUGCAnalytic(analytic: PlayShortsProductPickerUGCAnalytic): ContentProductTagAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsAnalyticSender(analyticSender: PlayShortsAnalyticSenderImpl): PlayShortsAnalyticSender

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
    abstract fun bindSetupProductAnalytic(setupProductAnalytic: PlayShortsSetupProductAnalyticImpl): PlayBroSetupProductAnalytic

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

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsEntryPointAnalytic(shortsEntryPointAnalytic: PlayShortsEntryPointAnalyticImpl): PlayShortsEntryPointAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayPerformanceDashboardEntryPointAnalytic(playPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayBroadcastBeautificationAnalytic(playBroadcastBeautificationAnalytic: PlayBroadcastBeautificationAnalyticImpl): PlayBroadcastBeautificationAnalytic

    @Binds
    @PlayShortsScope
    abstract fun bindPlayBroadcasterAnalyticSender(analytic: PlayBroadcasterAnalyticSenderImpl): PlayBroadcasterAnalyticSender

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
    abstract fun bindBroadcastingConfigStore(configStore: BroadcastingConfigStoreImpl): BroadcastingConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastScheduleConfigStore(configStore: BroadcastScheduleConfigStoreImpl): BroadcastScheduleConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastUGCConfigStore(configStore: AccountConfigStoreImpl): AccountConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindHydraConfigStore(configStore: HydraConfigStoreImpl): HydraConfigStore

    /**
     * Beautification
     */
    @Binds
    @PlayShortsScope
    abstract fun bindAssetChecker(assetChecker: AssetCheckerImpl): AssetChecker

    @Binds
    @PlayShortsScope
    abstract fun bindAssetManager(assetManager: AssetManagerImpl): AssetManager

    @Binds
    @PlayShortsScope
    abstract fun bindContentCoachMarkSharedPref(contentCoachMarkSharedPref: ContentCoachMarkSharedPrefImpl): ContentCoachMarkSharedPref
}
