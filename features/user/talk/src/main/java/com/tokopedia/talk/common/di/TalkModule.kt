package com.tokopedia.talk.common.di

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.talk.common.data.TalkApi
import com.tokopedia.talk.common.data.TalkUrl
import com.tokopedia.talk.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk.inboxtalk.domain.mapper.GetInboxTalkMapper
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 8/29/18.
 */
@TalkScope
@Module
class TalkModule {

    @TalkScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @TalkScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
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
                                   userSession: UserSession
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSession):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideOkHttpClient(chuckInterceptor: ChuckInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            debugInterceptor: DebugInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(debugInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }

        val headerResponseInterceptor =
                HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)

        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(headerResponseInterceptor)

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

    @TalkScope
    @Provides
    fun provideGetInboxTalkUseCase(api: TalkApi,
                                   getInboxTalkMapper: GetInboxTalkMapper): GetInboxTalkUseCase {
        return GetInboxTalkUseCase(api, getInboxTalkMapper)
    }
}