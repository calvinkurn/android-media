package com.tokopedia.play.broadcaster.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.revamp.BroadcastManager
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtilImpl
import com.tokopedia.play.broadcaster.util.cover.PlayMinimumCoverImageTransformer
import com.tokopedia.play.broadcaster.util.helper.DefaultUriParser
import com.tokopedia.play.broadcaster.util.helper.UriParser
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.PlayWebSocketImpl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module(includes = [MediaUploaderModule::class])
class PlayBroadcastModule {

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }

    @ActivityRetainedScope
    @Provides
    fun provideBroadcaster(): Broadcaster {
        return BroadcastManager()
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
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
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
            localCacheHandler,
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
            accountAnalytic
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
    fun providePlayBroadcastMapper(htmlTextTransformer: HtmlTextTransformer, uriParser: UriParser): PlayBroadcastMapper {
        return PlayBroadcastUiMapper(htmlTextTransformer, uriParser)
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