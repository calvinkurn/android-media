package com.tokopedia.play.di

import android.content.Context
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
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.PlayWebSocketImpl
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.player.creator.DefaultExoPlayerCreator
import com.tokopedia.play_common.player.creator.ExoPlayerCreator
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.product.detail.common.VariantConstant.QUERY_VARIANT
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule(val mContext: Context) {

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
    fun provideUpdateCartCounterMutation(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @Provides
    @PlayScope
    @Named(QUERY_VARIANT)
    internal fun provideQueryVariant(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.variant_common.R.raw.gql_product_variant)
    }

    @Provides
    @PlayScope
    internal fun provideAddToCartUseCase(graphqlUseCase: GraphqlUseCase,
                                         atcMapper: AddToCartDataMapper,
                                         chosenAddressHelper: ChosenAddressRequestHelper): AddToCartUseCase {
        return AddToCartUseCase(graphqlUseCase, atcMapper, chosenAddressHelper)
    }

    @Provides
    @PlayScope
    fun provideTrackingQueue(): TrackingQueue {
        return TrackingQueue(mContext)
    }

    @Provides
    @PlayScope
    fun provideExoPlaybackExceptionParser(): ExoPlaybackExceptionParser {
        return ExoPlaybackExceptionParser()
    }

    @PlayScope
    @Provides
    fun provideRemoteConfig(): RemoteConfig {
        return FirebaseRemoteConfigImpl(mContext)
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
    fun providePlayAnalytic(userSession: UserSessionInterface, trackingQueue: TrackingQueue): PlayAnalytic {
        return PlayAnalytic(userSession, trackingQueue)
    }

    @PlayScope
    @Provides
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }

    @Provides
    fun provideWebSocket(userSession: UserSessionInterface, dispatchers: CoroutineDispatchers): PlayWebSocket {
        return PlayWebSocketImpl(
                OkHttpClient.Builder(),
                userSession,
                dispatchers
        )
    }

    @Provides
    fun provideCastContext(@ApplicationContext context: Context): CastContext = CastContext.getSharedInstance(context)

    @Provides
    fun provideCastPlayer(castContext: CastContext) = CastPlayer(castContext)

    @PlayScope
    @Provides
    fun provideCastAnalyticHelper(playAnalytic: PlayAnalytic): CastAnalyticHelper = CastAnalyticHelper(playAnalytic)
}