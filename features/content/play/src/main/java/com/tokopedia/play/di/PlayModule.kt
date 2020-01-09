package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.play.KEY_GROUPCHAT_PREFERENCES
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.DefaultCoroutineDispatcherProvider
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.util.PlayLifecycleObserver
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule {

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context): TokopediaPlayManager = TokopediaPlayManager.getInstance(ctx)

    @PlayScope
    @Provides
    fun providePlayLifecycleObserver(playManager: TokopediaPlayManager): PlayLifecycleObserver = PlayLifecycleObserver(playManager)

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
             TokopediaUrl.getInstance().GROUPCHAT, // TODO("change url to TokopediaUrl.getInstance().CHAT for prod")
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
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @PlayScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, KEY_GROUPCHAT_PREFERENCES)
    }

}