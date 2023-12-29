package com.tokopedia.play.broadcaster.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.play.broadcaster.data.api.BeautificationAssetApi
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created By : Jonathan Darwin on March 13, 2023
 */
@Module
class PlayBroadcastNetworkModule {

    @Provides
    @ActivityRetainedScope
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
    @ActivityRetainedScope
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
    @ActivityRetainedScope
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @ActivityRetainedScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(context, collector)
    }

    @Provides
    @ActivityRetainedScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @ActivityRetainedScope
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }
}
