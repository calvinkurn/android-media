package com.tokopedia.play.broadcaster.shorts.di

import android.content.Context
import androidx.work.WorkManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import com.tokopedia.play.broadcaster.util.helper.DefaultUriParser
import com.tokopedia.play.broadcaster.util.helper.UriParser
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
class PlayShortsModule(
    private val activityContext: Context
) {

    @Provides
    @PlayShortsScope
    fun provideActivityContext() = activityContext

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @PlayShortsScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

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

    /** Play Broadcaster Analytic */
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
        accountAnalytic: PlayBroadcastAccountAnalytic
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

    /** Play Broadcaster HtmlTextTransformer */
    @Provides
    @PlayShortsScope
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }

    @Provides
    @PlayShortsScope
    fun provideUriParser(): UriParser {
        return DefaultUriParser()
    }

    /** Play Broadcaster Mapper */
    @Provides
    @PlayShortsScope
    fun providePlayBroadcastMapper(htmlTextTransformer: HtmlTextTransformer, uriParser: UriParser): PlayBroadcastMapper {
        return PlayBroadcastUiMapper(htmlTextTransformer, uriParser)
    }

    /** Play Broadcaster Remote Config */
    @Provides
    @PlayShortsScope
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    /** Play Broadcaster Use Case */
    @Provides
    @PlayShortsScope
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }
}
