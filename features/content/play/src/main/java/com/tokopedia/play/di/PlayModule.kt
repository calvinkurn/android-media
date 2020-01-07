package com.tokopedia.play.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.CoroutineCallAdapterFactory
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.RiskAnalyticsInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
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
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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
        /**
         * // TODO("need confirmation about adding new header")
         * return CommonNetwork.createRetrofit(
         * context,
         * TokopediaUrl.getInstance().GROUPCHAT, // TODO("change url to TokopediaUrl.getInstance().CHAT for prod")
         * networkRouter,
         * userSession)
         */

        /**
         * TODO("temporary request")
         */
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
        tkpdOkHttpBuilder.addInterceptor(TkpdAuthInterceptor(context, networkRouter, userSession))
        tkpdOkHttpBuilder.addInterceptor(FingerprintInterceptor(networkRouter, userSession))
        tkpdOkHttpBuilder.addInterceptor(RiskAnalyticsInterceptor(context))
        tkpdOkHttpBuilder.addInterceptor {
            val newRequest: Request.Builder = it.request().newBuilder()
            newRequest.addHeader("Origin", "www.tokopedia.com")
            it.proceed(newRequest.build())
        }

        return Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().GROUPCHAT) // TODO("change url to TokopediaUrl.getInstance().CHAT for prod")
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(tkpdOkHttpBuilder.build()).build()

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