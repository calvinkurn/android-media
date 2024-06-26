package com.tokopedia.play.di

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.gms.cast.framework.CastContext
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.play.analytic.CastAnalyticHelper
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayDimensionTrackingHelper
import com.tokopedia.play.util.PlayCastHelper
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.share.PlayShareExperienceImpl
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.player.creator.DefaultExoPlayerCreator
import com.tokopedia.play_common.player.creator.ExoPlayerCreator
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.PlayChannelSSEImpl
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.PlayWebSocketImpl
import com.tokopedia.play_common.websocket.SocketDebounce
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule {

    @PlayScope
    @Provides
    fun providerExoPlayerCreator(@ApplicationContext ctx: Context): ExoPlayerCreator = DefaultExoPlayerCreator(ctx)

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context, creator: ExoPlayerCreator): PlayVideoManager = PlayVideoManager.getInstance(ctx, creator)

    @PlayScope
    @Provides
    fun providePlayVideoPlayerLifecycleObserver(activity: AppCompatActivity): PlayVideoPlayerObserver {
        return PlayVideoPlayerObserver(activity)
    }

    @PlayScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideGraphqlRepositoryCase(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PlayScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, KEY_GROUP_CHAT_PREFERENCES)
    }

    @PlayScope
    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(activity: AppCompatActivity): String {
        return GraphqlHelper.loadRawString(
            activity.resources,
            com.tokopedia.atc_common.R.raw.gql_update_cart_counter
        )
    }

    @Provides
    @PlayScope
    internal fun provideAddToCartUseCase(
        graphqlUseCase: GraphqlUseCase,
        atcMapper: AddToCartDataMapper,
        chosenAddressHelper: ChosenAddressRequestHelper
    ): AddToCartUseCase {
        return AddToCartUseCase(graphqlUseCase, atcMapper, chosenAddressHelper)
    }

    @Provides
    @PlayScope
    fun provideTrackingQueue(activity: AppCompatActivity): TrackingQueue {
        return TrackingQueue(activity)
    }

    @Provides
    @PlayScope
    fun provideExoPlaybackExceptionParser(): ExoPlaybackExceptionParser {
        return ExoPlaybackExceptionParser()
    }

    @PlayScope
    @Provides
    fun provideRemoteConfig(activity: AppCompatActivity): RemoteConfig {
        return FirebaseRemoteConfigImpl(activity)
    }

    @PlayScope
    @Provides
    fun providePlayChannelStateStorage(): PlayChannelStateStorage {
        return PlayChannelStateStorage()
    }

    @PlayScope
    @Provides
    fun providePlayVideoWrapperBuilder(@ApplicationContext context: Context): PlayVideoWrapper.Builder {
        return PlayVideoWrapper.Builder(context)
    }

    @Provides
    @PlayScope
    fun providePlayAnalytic(userSession: UserSessionInterface, trackingQueue: TrackingQueue, dimensionTrackingHelper: PlayDimensionTrackingHelper): PlayAnalytic {
        return PlayAnalytic(userSession, trackingQueue, dimensionTrackingHelper)
    }

    @PlayScope
    @Provides
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }

    @Provides
    fun provideWebSocket(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers,
        localCacheHandler: LocalCacheHandler,
        socketDebounce: SocketDebounce,
    ): PlayWebSocket {
        return PlayWebSocketImpl(
            OkHttpClient.Builder(),
            userSession,
            dispatchers,
            context,
            localCacheHandler,
            socketDebounce,
        )
    }

    @Provides
    @Nullable
    fun provideCastContext(@ApplicationContext context: Context): CastContext? = PlayCastHelper.getCastContext(context)

    @Provides
    @Nullable
    fun provideCastPlayer(castContext: CastContext?): CastPlayer? = castContext?.let { CastPlayer(it) }

    @PlayScope
    @Provides
    fun provideCastAnalyticHelper(playAnalytic: PlayAnalytic): CastAnalyticHelper = CastAnalyticHelper(playAnalytic)

    /**
     * SSE
     */
    @PlayScope
    @Provides
    fun providePlaySSE(
        @ApplicationContext appContext: Context,
        userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers,
        socketDebounce: SocketDebounce,
    ): PlayChannelSSE =
        PlayChannelSSEImpl(userSession, dispatchers, appContext, socketDebounce)

    /**
     * Sharing Experience
     */
    @PlayScope
    @Provides
    fun providePlayShareExperience(@ApplicationContext context: Context): PlayShareExperience =
        PlayShareExperienceImpl(context)

    @PlayScope
    @Provides
    fun providePlayMetrics(): PlayLiveRoomMetricsCommon = PlayLiveRoomMetricsCommon()

    @PlayScope
    @Provides
    fun provideWidgetTools(
        playWidgetUseCase: PlayWidgetUseCase,
        playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
        playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
        mapper: PlayWidgetUiMapper,
        connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(
            playWidgetUseCase,
            playWidgetReminderUseCase,
            playWidgetUpdateChannelUseCase,
            mapper,
            connectionUtil
        )
    }

    @PlayScope
    @Provides
    fun provideActivityResultRegistry(activity: AppCompatActivity): ActivityResultRegistry {
        return activity.activityResultRegistry
    }

    @Provides
    @PlayScope
    fun provideABTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}
