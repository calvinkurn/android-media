package com.tokopedia.talk.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.talk.feature.reporttalk.data.TalkApi
import com.tokopedia.talk.feature.reporttalk.data.TalkUrl
import com.tokopedia.talk.feature.reporttalk.network.TalkErrorResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class TalkModule {

    @TalkScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @TalkScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TalkScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @TalkScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @TalkScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideOkHttpClient(chuckInterceptor: ChuckerInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            debugInterceptor: DebugInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
        builder.addInterceptor(ErrorResponseInterceptor(TalkErrorResponse::class.java))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(debugInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @TalkScope
    @Provides
    fun provideTalkRetrofit(retrofitBuilder: Retrofit.Builder,
                            @TalkScope okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(TalkUrl.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @TalkScope
    @Provides
    fun provideTalkApi(@TalkScope retrofit: Retrofit): TalkApi {
        return retrofit.create(TalkApi::class.java)
    }
}