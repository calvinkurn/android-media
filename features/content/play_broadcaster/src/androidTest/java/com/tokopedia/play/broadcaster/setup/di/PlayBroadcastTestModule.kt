package com.tokopedia.play.broadcaster.setup.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
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
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtilImpl
import com.tokopedia.play.broadcaster.util.cover.PlayMinimumCoverImageTransformer
import com.tokopedia.play.broadcaster.util.helper.DefaultUriParser
import com.tokopedia.play.broadcaster.util.helper.UriParser
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapper
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.PlayWebSocketImpl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Module(includes = [MediaUploaderModule::class])
class PlayBroadcastTestModule(
    private val activityContext: Context,
    private val mockUserSession: UserSessionInterface,
    private val mockBroadcaster: Broadcaster,
    private val mockCoachMarkSharedPref: ContentCoachMarkSharedPref,
    private val mockBroadcastTimer: PlayBroadcastTimer,
    private val mockGetChannelUseCase: GetChannelUseCase,
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase,
    private val mockValueWrapper: PlayBroadcastValueWrapper
) {

    @Provides
    @ActivityRetainedScope
    fun provideActivityContext() = activityContext

    @ActivityRetainedScope
    @Provides
    fun provideUserSessionInterface(): UserSessionInterface = mockUserSession

    @ActivityRetainedScope
    @Provides
    fun provideBroadcaster(): Broadcaster = mockBroadcaster

    @ActivityRetainedScope
    @Provides
    fun provideContentCoachMarkSharedPref(): ContentCoachMarkSharedPref = mockCoachMarkSharedPref

    @ActivityRetainedScope
    @Provides
    fun provideBroadcastTimer(): PlayBroadcastTimer = mockBroadcastTimer

    @ActivityRetainedScope
    @Provides
    fun provideGetChannelUseCase(): GetChannelUseCase = mockGetChannelUseCase

    @ActivityRetainedScope
    @Provides
    fun provideGetAddedChannelTagsUseCase(): GetAddedChannelTagsUseCase = mockGetAddedTagUseCase

    @ActivityRetainedScope
    @Provides
    fun provideValueWrapper(): PlayBroadcastValueWrapper = mockValueWrapper

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, KEY_GROUP_CHAT_PREFERENCES)
    }

    @ActivityRetainedScope
    @Provides
    fun provideWebSocket(
        userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers,
        localCacheHandler: LocalCacheHandler,
        @ApplicationContext context: Context
    ): PlayWebSocket {
        return PlayWebSocketImpl(
            OkHttpClient.Builder(),
            userSession,
            dispatchers,
            context,
            localCacheHandler
        )
    }

    @ActivityRetainedScope
    @Provides
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
        performanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic,
        beautificationAnalytic: PlayBroadcastBeautificationAnalytic
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
            performanceDashboardEntryPointAnalytic,
            beautificationAnalytic
        )
    }

    @ActivityRetainedScope
    @Provides
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }

    @ActivityRetainedScope
    @Provides
    fun provideUriParser(): UriParser {
        return DefaultUriParser()
    }

    @ActivityRetainedScope
    @Provides
    fun providePlayBroadcastMapper(htmlTextTransformer: HtmlTextTransformer, uriParser: UriParser, assetChecker: AssetChecker): PlayBroadcastMapper {
        return PlayBroadcastUiMapper(htmlTextTransformer, uriParser, assetChecker)
    }

    @ActivityRetainedScope
    @Provides
    fun provideCoverImageUtil(@ApplicationContext context: Context): PlayCoverImageUtil = PlayCoverImageUtilImpl(context)

    @ActivityRetainedScope
    @Provides
    fun provideCoverImageTransformer(): ImageTransformer = PlayMinimumCoverImageTransformer()

    @ActivityRetainedScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}
