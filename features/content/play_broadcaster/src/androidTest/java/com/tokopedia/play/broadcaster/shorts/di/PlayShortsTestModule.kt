package com.tokopedia.play.broadcaster.shorts.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.util.Router
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.view.manager.idle.PlayShortsIdleManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@Module
class PlayShortsTestModule(
    private val activityContext: Context,
    private val mockShortsRepo: PlayShortsRepository,
    private val mockBroRepo: PlayBroadcastRepository,
    private val mockProductTagRepo: ProductTagRepository,
    private val mockUgcOnboardingRepo: UGCOnboardingRepository,
    private val mockUserSession: UserSessionInterface,
    private val mockAccountManager: PlayShortsAccountManager,
    private val mockRouter: Router,
    private val mockIdleManager: PlayShortsIdleManager,
) {

    @Provides
    @PlayShortsScope
    fun provideActivityContext() = activityContext

    @Provides
    @PlayShortsScope
    fun providePlayShortsRepository(): PlayShortsRepository = mockShortsRepo

    @Provides
    @PlayShortsScope
    fun provideUserSessionInterface(): UserSessionInterface = mockUserSession

    @Provides
    @PlayShortsScope
    fun providePlayBroRepository(): PlayBroadcastRepository = mockBroRepo

    @Provides
    fun provideProductTagRepository(): ProductTagRepository = mockProductTagRepo

    @Provides
    fun provideUGCOnboardingRepository(): UGCOnboardingRepository = mockUgcOnboardingRepo

    @Provides
    fun provideAccountManager(): PlayShortsAccountManager = mockAccountManager

    @Provides
    fun provideRouter(): Router = mockRouter

    @Provides
    fun provideIdleManager(): PlayShortsIdleManager = mockIdleManager

    @Provides
    @PlayShortsScope
    fun provideExoPlayer(): ExoPlayer {
        return SimpleExoPlayer.Builder(activityContext)
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    @Provides
    @PlayShortsScope
    fun provideProgressiveMediaSourceFactory(): ProgressiveMediaSource.Factory {
        return ProgressiveMediaSource
            .Factory(
                DefaultDataSourceFactory(
                    activityContext,
                    Util.getUserAgent(activityContext, "Tokopedia Android")
                )
            )
    }

    @Provides
    @PlayShortsScope
    fun providePlayBroadcastAnalytic(
        userSession: UserSessionInterface,
        interactiveAnalytic: PlayBroadcastInteractiveAnalytic,
        setupMenuAnalytic: PlayBroSetupMenuAnalytic,
        setupTitleAnalytic: PlayBroSetupTitleAnalytic,
        setupCoverAnalytic: PlayBroSetupCoverAnalytic,
        setupProductAnalytic: PlayBroSetupProductAnalytic,
        summaryAnalytic: PlayBroadcastSummaryAnalytic,
        scheduleAnalytic: PlayBroScheduleAnalytic,
        pinProductAnalytic: PlayBroadcastPinProductAnalytic,
        accountAnalytic: PlayBroadcastAccountAnalytic,
        shortsEntryPointAnalytic: PlayShortsEntryPointAnalytic,
    ): PlayBroadcastAnalytic {
        return PlayBroadcastAnalytic(
            userSession,
            interactiveAnalytic,
            setupMenuAnalytic,
            setupTitleAnalytic,
            setupCoverAnalytic,
            setupProductAnalytic,
            summaryAnalytic,
            scheduleAnalytic,
            pinProductAnalytic,
            accountAnalytic,
            shortsEntryPointAnalytic,
        )
    }
}
