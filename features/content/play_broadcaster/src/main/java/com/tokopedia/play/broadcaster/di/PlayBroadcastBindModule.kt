package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalyticImpl
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalytic
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalyticImpl
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountUpTimerImpl
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimer
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.countup.PlayCountUp
import com.tokopedia.play.broadcaster.util.countup.PlayCountUpImpl
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
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
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): PlayBroadcastDialogCustomizer

    /**
     * Analytic
     */
    @Binds
    @ActivityRetainedScope
    abstract fun bindContentTaggingAnalytic(contentTaggingAnalytic: PlayBroadcastContentTaggingAnalyticImpl): PlayBroadcastContentTaggingAnalytic

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


    @ActivityRetainedScope
    @Binds
    abstract fun bindLogger(logger: PlayLoggerImpl): PlayLogger

    /**
     * Pusher
     */

    @ActivityRetainedScope
    @Binds
    abstract fun bindPlayLivePusherCountUpTimer(playLivePusherCountUpTimerImpl: PlayLivePusherCountUpTimerImpl): PlayLivePusherTimer

    @ActivityRetainedScope
    @Binds
    abstract fun bindPlayCountUp(playCountUpImpl: PlayCountUpImpl): PlayCountUp
}