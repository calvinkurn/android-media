package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.KEY_GROUPCHAT_PREFERENCES
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChannelResponseMapper
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.creator.DefaultExoPlayerCreator
import com.tokopedia.play_common.player.creator.ExoPlayerCreator
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.coroutine.DefaultCoroutineDispatcherProvider
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.constant.VariantConstant
import dagger.Module
import dagger.Provides
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
    fun providerDispatcherProvider(): CoroutineDispatcherProvider = DefaultCoroutineDispatcherProvider()

    @PlayScope
    @Provides
    fun provideGraphqlRepositoryCase(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PlayScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, KEY_GROUPCHAT_PREFERENCES)
    }

    @PlayScope
    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @Provides
    @PlayScope
    @Named(VariantConstant.QUERY_VARIANT)
    internal fun provideQueryVariant(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.variant_common.R.raw.gql_product_variant)
    }

    @Provides
    @PlayScope
    @Named(AtcConstant.MUTATION_ADD_TO_CART)
    internal fun provideAddToCartMutation(): String {
        return GraphqlHelper.loadRawString(mContext.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
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
    fun providePlayChannelResponseMapper(): PlayChannelResponseMapper {
        return PlayChannelDetailsWithRecomMapper()
    }
}