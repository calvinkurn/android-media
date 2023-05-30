package com.tokopedia.play.broadcaster.setup.di

import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.checker.AssetCheckerImpl
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManagerImpl
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
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
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalyticImpl
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
import com.tokopedia.play.broadcaster.analytic.ugc.ProductPickerUGCAnalytic
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimerImpl
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.countup.PlayCountUp
import com.tokopedia.play.broadcaster.util.countup.PlayCountUpImpl
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play.broadcaster.view.scale.BroadcasterFrameScalingManager
import com.tokopedia.play.broadcaster.view.scale.BroadcasterFrameScalingManagerImpl
import com.tokopedia.play_common.util.device.PlayDeviceSpec
import com.tokopedia.play_common.util.device.PlayDeviceSpecImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Module
abstract class PlayBroadcastBindTestModule {

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
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): PlayBroadcastDialogCustomizer

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
    abstract fun bindSetupProductAnalytic(setupProductAnalytic: PlayBroSetupProductAnalyticImpl): PlayBroSetupProductAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindSummaryAnalytic(summaryAnalytic: PlayBroadcastSummaryAnalyticImpl): PlayBroadcastSummaryAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindScheduleAnalytic(scheduleAnalytic: PlayBroScheduleAnalyticImpl): PlayBroScheduleAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPinProductAnalytic(pinProductAnalytic: PlayBroadcastPinProductAnalyticImpl): PlayBroadcastPinProductAnalytic

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
    abstract fun bindPlayPerformanceDashboardEntryPointAnalytic(analytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroadcastBeautificationAnalytic(analytic: PlayBroadcastBeautificationAnalyticImpl): PlayBroadcastBeautificationAnalytic

    @Binds
    @ActivityRetainedScope
    abstract fun bindPlayBroadcasterAnalyticSender(analytic: PlayBroadcasterAnalyticSenderImpl): PlayBroadcasterAnalyticSender

    @ActivityRetainedScope
    @Binds
    abstract fun bindLogger(logger: PlayLoggerImpl): PlayLogger

    /**
     * Pusher
     */
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
}
