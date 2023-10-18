package com.tokopedia.play.di

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.Nullable
import androidx.core.app.ActivityOptionsCompat
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
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.player.creator.DefaultExoPlayerCreator
import com.tokopedia.play_common.player.creator.ExoPlayerCreator
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.PlayChannelSSEImpl
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@Module
class PlayTestModule(
    val mContext: Context,
    val trackingQueue: TrackingQueue = TrackingQueue(mContext),
    val userSession: (appContext: Context) -> UserSessionInterface = { UserSession(it) },
    val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(mContext),
    val playPreference: (appContext: Context) -> PlayPreference = {
        PlayPreference(
            it,
            userSession.invoke(it)
        )
    }
) {

    @PlayScope
    @Provides
    fun providerExoPlayerCreator(@ApplicationContext ctx: Context): ExoPlayerCreator = DefaultExoPlayerCreator(ctx)

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context, creator: ExoPlayerCreator): PlayVideoManager = PlayVideoManager.getInstance(ctx, creator)

    @PlayScope
    @Provides
    fun providePlayVideoPlayerLifecycleObserver(): PlayVideoPlayerObserver = PlayVideoPlayerObserver(mContext)

    @PlayScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return userSession(context)
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
    fun provideUpdateCartCounterMutation(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
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
    fun provideTrackingQueue(): TrackingQueue {
        return trackingQueue
    }

    @Provides
    @PlayScope
    fun provideExoPlaybackExceptionParser(): ExoPlaybackExceptionParser {
        return ExoPlaybackExceptionParser()
    }

    @PlayScope
    @Provides
    fun provideRemoteConfig(): RemoteConfig {
        return remoteConfig
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
    fun providePlaySSE(userSession: UserSessionInterface, dispatchers: CoroutineDispatchers): PlayChannelSSE =
        PlayChannelSSEImpl(userSession, dispatchers, mContext)

    /**
     * Sharing Experience
     */
    @PlayScope
    @Provides
    fun providePlayShareExperience(@ApplicationContext context: Context): PlayShareExperience =
        PlayShareExperienceImpl(context)

    @PlayScope
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): PlayPreference {
        return playPreference(context)
    }

    private val resultRegistry = object : ActivityResultRegistry() {
        override fun <I : Any?, O : Any?> onLaunch(
            requestCode: Int,
            contract: ActivityResultContract<I, O>,
            input: I,
            options: ActivityOptionsCompat?
        ) {
            dispatchResult(requestCode, null)
        }
    }

    @PlayScope
    @Provides
    fun provideActivityResultRegistry(): ActivityResultRegistry {
        return resultRegistry
    }
}
