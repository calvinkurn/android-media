package com.tokopedia.topchat.chattemplate.di

import android.content.Context
import com.tokopedia.topchat.chatlist.data.TopChatUrl.Companion.BASE_URL
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.UserSession
import com.tokopedia.network.NetworkRouter
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.chat.api.ChatApiKt
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImplKt
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryKt
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepositoryImplKt
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepositoryKt
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

/**
 * Created by stevenfredian on 9/14/17.
 */
@Module
class TemplateChatModule {

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ActivityScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @Provides
    fun provideChatTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun provideOkHttpClient(
        @InboxQualifier retryPolicy: OkHttpRetryPolicy,
        errorResponseInterceptor: ErrorResponseInterceptor,
        chuckInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface,
        tkpdAuthInterceptor: TkpdAuthInterceptor
    ): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(FingerprintInterceptor(networkRouter, userSessionInterface))
            .addInterceptor(tkpdAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @ActivityScope
    @InboxQualifier
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @ActivityScope
    @InboxQualifier
    @Provides
    fun provideChatRetrofit(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(BASE_URL)
            .addConverterFactory(TokopediaWsV4ResponseConverter())
            .addConverterFactory(StringResponseConverter())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @ActivityScope
    @Provides
    fun provideChatApi(@InboxQualifier retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideChatApiKt(@InboxQualifier retrofit: Retrofit): ChatApiKt {
        return retrofit.create(ChatApiKt::class.java)
    }

    @ActivityScope
    @Provides
    fun provideTemplateRepositoryKt(templateRepositoryImplKt: TemplateRepositoryImplKt): TemplateRepositoryKt {
        return templateRepositoryImplKt
    }

    @ActivityScope
    @Provides
    fun provideEditTemplateRepositoryKt(editTemplateRepositoryImplKt: EditTemplateRepositoryImplKt): EditTemplateRepositoryKt {
        return editTemplateRepositoryImplKt
    }

    companion object {
        private const val NET_READ_TIMEOUT = 60
        private const val NET_WRITE_TIMEOUT = 60
        private const val NET_CONNECT_TIMEOUT = 60
        private const val NET_RETRY = 1
    }
}