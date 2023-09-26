package com.tokopedia.sellerfeedback.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.sellerfeedback.di.scope.SellerFeedbackScope
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
class SellerFeedbackNetworkModule {

    @Provides
    @SellerFeedbackScope
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

//        if (GlobalConfig.isAllowDebuggingTools()) {
//            clientBuilder.addInterceptor(debugInterceptor)
//            clientBuilder.addInterceptor(chuckInterceptor)
//        }

        return clientBuilder.build()
    }

    @Provides
    @SellerFeedbackScope
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @SellerFeedbackScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(context, collector)
    }

    @Provides
    @SellerFeedbackScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @SellerFeedbackScope
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }
}
