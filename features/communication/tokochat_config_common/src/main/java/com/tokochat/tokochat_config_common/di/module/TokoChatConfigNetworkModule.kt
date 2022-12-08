package com.tokochat.tokochat_config_common.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.data.model.response.TkpdV4ResponseError
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object TokoChatConfigNetworkModule {

    private const val NET_READ_TIMEOUT = 60
    private const val NET_WRITE_TIMEOUT = 60
    private const val NET_CONNECT_TIMEOUT = 60
    private const val NET_RETRY = 3

    private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    @Provides
    @TokoChatQualifier
    fun provideRetrofitBuilder(@TokoChatQualifier gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @Provides
    @TokoChatQualifier
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(GSON_DATE_FORMAT)
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    @Provides
    @TokoChatQualifier
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return logging
    }

    @Provides
    @TokoChatQualifier
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @Provides
    @TokoChatQualifier
    fun provideOkHttpClient(
        @TokoChatQualifier retryPolicy: OkHttpRetryPolicy,
        @TokoChatQualifier loggingInterceptor: HttpLoggingInterceptor,
        @TokoChatQualifier errorResponseInterceptor: ErrorResponseInterceptor,
        @TokoChatQualifier chuckerInterceptor: ChuckerInterceptor,
        @TokoChatQualifier akamaiBotInterceptor: AkamaiBotInterceptor,
        @TokoChatQualifier tkpdAuthInterceptor: TkpdAuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorResponseInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(akamaiBotInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }

        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @Provides
    @TokoChatQualifier
    fun provideChatRetrofit(
        @TokoChatQualifier retrofitBuilder: Retrofit.Builder,
        @TokoChatQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(TokopediaUrl.getInstance().TOKOCHAT)
            .addConverterFactory(StringResponseConverter())
            .client(okHttpClient).build()
    }

    @Provides
    @TokoChatQualifier
    fun provideChuckerInterceptor(
        @TokoChatQualifier context: Context
    ): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @TokoChatQualifier
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @Provides
    @TokoChatQualifier
    fun provideNetworkRouter(@TokoChatQualifier context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @TokoChatQualifier
    fun provideTkpdAuthInterceptor(
        @TokoChatQualifier context: Context,
        @TokoChatQualifier networkRouter: NetworkRouter,
        @TokoChatQualifier userSessionInterface: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @Provides
    @TokoChatQualifier
    fun provideAkamaiBotInterceptor(@TokoChatQualifier context: Context): AkamaiBotInterceptor {
        return AkamaiBotInterceptor(context)
    }
}
