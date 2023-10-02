package com.tokopedia.inbox.universalinbox.stub.di.tokochat

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.data.model.response.TkpdV4ResponseError
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.interceptor.ErrorResponseInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object UniversalInboxTokoChatNetworkModuleStub {

    const val PORT_NUMBER = 8090

    private const val NET_READ_TIMEOUT = 1
    private const val NET_WRITE_TIMEOUT = 1
    private const val NET_CONNECT_TIMEOUT = 1
    private const val NET_RETRY = 1

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
            .setLenient()
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
        @TokoChatQualifier chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorResponseInterceptor)
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
            .baseUrl("http://localhost:$PORT_NUMBER/") // local base url
            .addConverterFactory(StringResponseConverter())
            .client(okHttpClient).build()
    }

    @Provides
    @TokoChatQualifier
    fun provideChuckerInterceptor(
        @TokoChatQualifier context: Context
    ): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context,
            true
        )
        collector.showNotification = true
        return ChuckerInterceptor(context, collector)
    }

    @Provides
    @TokoChatQualifier
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }
}
