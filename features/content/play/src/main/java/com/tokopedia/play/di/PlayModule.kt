package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.play.KEY_GROUPCHAT_PREFERENCES
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.DefaultCoroutineDispatcherProvider
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.util.PlayLifecycleObserver
import com.tokopedia.play_common.util.PlayProcessLifecycleObserver
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.constant.VariantConstant
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule(val mContext: Context) {

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context): PlayVideoManager = PlayVideoManager.getInstance(ctx)

    @PlayScope
    @Provides
    fun providePlayLifecycleObserver(): PlayLifecycleObserver = PlayLifecycleObserver(mContext)

    @PlayScope
    @Provides
    fun providePlayProcessLifecycleObserver(): PlayProcessLifecycleObserver = PlayProcessLifecycleObserver(mContext)

    @PlayScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        if (context is NetworkRouter) {
            return context
        }
        throw IllegalStateException("Application must implement NetworkRouter")
    }

    @PlayScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, userSession: UserSession, networkRouter: NetworkRouter): Retrofit {
         return CommonNetwork.createRetrofit(
             context,
             TokopediaUrl.getInstance().PLAY,
             networkRouter,
             userSession)
    }

    @PlayScope
    @Provides
    fun providerDispatcherProvider(): CoroutineDispatcherProvider = DefaultCoroutineDispatcherProvider()

    @PlayScope
    @Provides
    fun providePlayApi(retrofit: Retrofit): PlayApi {
        return retrofit.create(PlayApi::class.java)
    }

    @PlayScope
    @Provides
    fun providesGraphqlUsecase(): GraphqlUseCase = GraphqlUseCase()

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
}