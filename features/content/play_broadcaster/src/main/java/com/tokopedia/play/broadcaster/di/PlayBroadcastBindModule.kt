package com.tokopedia.play.broadcaster.di

import com.tokopedia.byteplus.effect.EffectManager
import com.tokopedia.byteplus.effect.EffectManagerImpl
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.checker.AssetCheckerImpl
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManagerImpl
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
import com.tokopedia.content.product.picker.ugc.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.common.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalytic
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.sender.PlayBroadcasterAnalyticSender
import com.tokopedia.play.broadcaster.analytic.sender.PlayBroadcasterAnalyticSenderImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.ugc.ProductPickerUGCAnalytic
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimerImpl
import com.tokopedia.play.broadcaster.util.countup.PlayCountUp
import com.tokopedia.play.broadcaster.util.countup.PlayCountUpImpl
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
import com.tokopedia.play.broadcaster.util.logger.error.BroadcasterErrorLogger
import com.tokopedia.play.broadcaster.util.logger.error.BroadcasterErrorLoggerImpl
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapper
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapperImpl
import com.tokopedia.play.broadcaster.view.scale.BroadcasterFrameScalingManager
import com.tokopedia.play.broadcaster.view.scale.BroadcasterFrameScalingManagerImpl
import com.tokopedia.play_common.util.device.PlayDeviceSpec
import com.tokopedia.play_common.util.device.PlayDeviceSpecImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PlayBroadcastBindModule {

    /**
     * Pref
     */
    @Binds
    @ActivityRetainedScope
    abstract fun bindPermissionSharedPrefs(sharedPref: HydraSharedPreferences): PermissionSharedPreferences

    /**
     * Util
     */
    @Binds
    @ActivityRetainedScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): ContentDialogCustomizer

    /**
     * Analytic
     */
    @Binds
    @ActivityRetainedScope
    abstract fun bindInteractiveAnalytic(interactiveAnalytic: PlayBroadcastInteractiveAnalyticImpl): PlayBroadcastInteractiveAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSetupMenuAnalytic(setupMenuAnalytic: PlayBroSetupMenuAnalyticImpl): PlayBroSetupMenuAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSetupTitleAnalytic(setupTitleAnalytic: PlayBroSetupTitleAnalyticImpl): PlayBroSetupTitleAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSetupCoverAnalytic(setupCoverAnalytic: PlayBroSetupCoverAnalyticImpl): PlayBroSetupCoverAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSetupProductAnalytic(setupProductAnalytic: PlayBroSetupProductAnalyticImpl): ContentProductPickerSellerAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSummaryAnalytic(summaryAnalytic: PlayBroadcastSummaryAnalyticImpl): PlayBroadcastSummaryAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindScheduleAnalytic(scheduleAnalytic: PlayBroScheduleAnalyticImpl): PlayBroScheduleAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPinProductAnalytic(pinProductAnalytic: PlayBroadcastPinProductAnalyticImpl): ContentPinnedProductAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPinUGCAnalytic(ugcAnalytic: PlayBroadcastAccountAnalyticImpl): PlayBroadcastAccountAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayShortsEntryPointAnalytic(shortsEntryPointAnalytic: PlayShortsEntryPointAnalyticImpl): PlayShortsEntryPointAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindProductPickerUGCAnalytic(productPickerUGCAnalytic: ProductPickerUGCAnalytic): ContentProductTagAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroCoverPickerAnalytic(analytic: PlayBroCoverPickerAnalyticImpl): PlayBroCoverPickerAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayPerformanceDashboardAnalytic(analytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroadcastBeautificationAnalytic(analytic: PlayBroadcastBeautificationAnalyticImpl): PlayBroadcastBeautificationAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroadcasterAnalyticSender(analytic: PlayBroadcasterAnalyticSenderImpl): PlayBroadcasterAnalyticSender


    /**
     * Logger
     */
    @ActivityRetainedScope
    @Binds
    abstract fun bindLogger(logger: PlayLoggerImpl): PlayLogger

    @ActivityRetainedScope
    @Binds
    abstract fun bindBroadcasterErrorLogger(logger: BroadcasterErrorLoggerImpl): BroadcasterErrorLogger

    /**
     * Pusher
     */

    @ActivityRetainedScope
    @Binds
    abstract fun bindPlayBroadcastTimer(broadcastTimer: PlayBroadcastTimerImpl): PlayBroadcastTimer

    @ActivityRetainedScope
    @Binds
    abstract fun bindPlayCountUp(playCountUpImpl: PlayCountUpImpl): PlayCountUp

    /**
     * Scale
     */
    @ActivityRetainedScope
    @Binds
    abstract fun bindBroadcasterFrameScalingManager(broadcasterFrameScalingManager: BroadcasterFrameScalingManagerImpl): BroadcasterFrameScalingManager

    /**
     * Device Spec
     */
    @ActivityRetainedScope
    @Binds
    abstract fun bindPlayDeviceSpec(playDeviceSpec: PlayDeviceSpecImpl): PlayDeviceSpec

    /**
     * Beautification
     */
    @ActivityRetainedScope
    @Binds
    abstract fun bindAssetChecker(assetChecker: AssetCheckerImpl): AssetChecker

    @Binds
    @ActivityRetainedScope
    abstract fun bindAssetManager(assetManager: AssetManagerImpl): AssetManager

    @Binds
    @ActivityRetainedScope
    abstract fun bindEffectManager(effectManager: EffectManagerImpl): EffectManager

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroadcastValueWrapper(valueWrapper: PlayBroadcastValueWrapperImpl): PlayBroadcastValueWrapper
}
