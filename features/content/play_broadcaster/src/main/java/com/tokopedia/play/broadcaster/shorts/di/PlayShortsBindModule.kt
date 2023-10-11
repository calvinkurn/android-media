package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
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
import com.tokopedia.play.broadcaster.data.repository.*
import com.tokopedia.play.broadcaster.domain.repository.*
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
import com.tokopedia.play.broadcaster.shorts.data.PlayShortsRepositoryImpl
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManagerImpl
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsUiMapper
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.checker.AssetCheckerImpl
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManagerImpl
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play_common.util.device.PlayDeviceSpec
import com.tokopedia.play_common.util.device.PlayDeviceSpecImpl
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

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsAccountManager(accountManager: PlayShortsAccountManagerImpl): PlayShortsAccountManager

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
    abstract fun bindPlayPerformanceDashboardAnalytic(analytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic

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
    abstract fun bindBeautificationRepository(
        repo: PlayBroadcastBeautificationRepositoryImpl
    ): PlayBroadcastBeautificationRepository

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
    abstract fun bindPlayBroadcastBeautificationAnalytic(analytic: PlayBroadcastBeautificationAnalyticImpl): PlayBroadcastBeautificationAnalytic

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
    abstract fun bindBroadcastScheduleConfigStore(configStore: BroadcastScheduleConfigStoreImpl): BroadcastScheduleConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastUGCConfigStore(configStore: AccountConfigStoreImpl): AccountConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastingConfigStore(configStore: BroadcastingConfigStoreImpl): BroadcastingConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindHydraConfigStore(configStore: HydraConfigStoreImpl): HydraConfigStore

    @Binds
    @PlayShortsScope
    abstract fun bindSetupDataStore(dataStore: PlayBroadcastSetupDataStoreImpl): PlayBroadcastSetupDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindDataStore(dataStore: PlayBroadcastDataStoreImpl): PlayBroadcastDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindCoverDataStore(dataStore: CoverDataStoreImpl): CoverDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindTitleDataStore(dataStore: TitleDataStoreImpl): TitleDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindTagsDataStore(dataStore: TagsDataStoreImpl): TagsDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindBroadcastScheduleDataStore(dataStore: BroadcastScheduleDataStoreImpl): BroadcastScheduleDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindInteractiveDataStore(dataStore: InteractiveDataStoreImpl): InteractiveDataStore

    @Binds
    @PlayShortsScope
    abstract fun bindProductTagDataStore(dataStore: ProductTagDataStoreImpl): ProductTagDataStore


    /**
     * Device SPec
     */
    @Binds
    @PlayShortsScope
    abstract fun bindPlayDeviceSpec(playDeviceSpec: PlayDeviceSpecImpl): PlayDeviceSpec

    /**
     * Beautification
     */
    @Binds
    @PlayShortsScope
    abstract fun bindAssetChecker(assetChecker: AssetCheckerImpl): AssetChecker

    @Binds
    @PlayShortsScope
    abstract fun bindAssetManager(assetManager: AssetManagerImpl): AssetManager
}
