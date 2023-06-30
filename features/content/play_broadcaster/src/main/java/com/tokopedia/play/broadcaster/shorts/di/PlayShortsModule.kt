package com.tokopedia.play.broadcaster.shorts.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
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
import com.tokopedia.play.broadcaster.data.api.BeautificationAssetApi
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.play.broadcaster.util.helper.DefaultUriParser
import com.tokopedia.play.broadcaster.util.helper.UriParser
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

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
        accountAnalytic: PlayBroadcastAccountAnalytic,
        shortsEntryPointAnalytic: PlayShortsEntryPointAnalytic,
        playBroadcastPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic,
        beautificationAnalytic: PlayBroadcastBeautificationAnalytic,
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
            playBroadcastPerformanceDashboardEntryPointAnalytic,
            beautificationAnalytic,
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
    fun providePlayBroadcastMapper(htmlTextTransformer: HtmlTextTransformer, uriParser: UriParser, assetChecker: AssetChecker): PlayBroadcastMapper {
        return PlayBroadcastUiMapper(htmlTextTransformer, uriParser, assetChecker)
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

    /** Play Broadcaster Network */
    @Provides
    @PlayShortsScope
    fun provideBroadcastBeautificationApi(
        builder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): BeautificationAssetApi {
        return builder
            .baseUrl(TokopediaUrl.Companion.getInstance().GQL)
            .client(okHttpClient)
            .build()
            .create(BeautificationAssetApi::class.java)
    }

    @Provides
    @PlayShortsScope
    fun provideOkHttpClient(
        okHttpRetryPolicy: OkHttpRetryPolicy,
        chuckInterceptor: ChuckerInterceptor,
        debugInterceptor: DebugInterceptor,
        tkpdAuthInterceptor: TkpdAuthInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(tkpdAuthInterceptor)
            .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(debugInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }

        return clientBuilder.build()
    }

    @Provides
    @PlayShortsScope
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @PlayShortsScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(context, collector)
    }

    @Provides
    @PlayShortsScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @PlayShortsScope
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @Provides
    @PlayShortsScope
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}
