package com.tokopedia.tokochat.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
object TokoChatImageAttachmentNetworkModule {

    private const val NET_READ_TIMEOUT = 300
    private const val NET_WRITE_TIMEOUT = 300
    private const val NET_CONNECT_TIMEOUT = 300
    private const val NET_RETRY = 3

    private const val RETROFIT_TOKOCHAT_DOWNLOAD_IMAGE = "retrofit_tokochat_download_image"
    private const val OKHTTP_TOKOCHAT_DOWNLOAD_IMAGE = "okhttp_tokochat_download_image"

    @TokoChatScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @TokoChatScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @TokoChatScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @TokoChatScope
    @Provides
    fun provideTokoChatImageApi(@TokoChatQualifier retrofit: Retrofit): TokoChatImageApi {
        return retrofit.create(TokoChatImageApi::class.java)
    }

    // Download Image Section

    @TokoChatScope
    @Provides
    @Named(OKHTTP_TOKOCHAT_DOWNLOAD_IMAGE)
    fun provideOkHttpClientDownloadImage(
        retryPolicy: OkHttpRetryPolicy,
        loggingInterceptor: HttpLoggingInterceptor,
        errorResponseInterceptor: ErrorResponseInterceptor,
        chuckerInterceptor: ChuckerInterceptor
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

    @TokoChatScope
    @Provides
    @Named(RETROFIT_TOKOCHAT_DOWNLOAD_IMAGE)
    fun provideChatRetrofitDownloadImage(
        retrofitBuilder: Retrofit.Builder,
        @Named(OKHTTP_TOKOCHAT_DOWNLOAD_IMAGE) okHttpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder
            .baseUrl("https://www.tokopedia.com/")
            .addConverterFactory(StringResponseConverter())
            .client(okHttpClient).build()
    }

    @TokoChatScope
    @Provides
    fun provideTokoChatImageApiDownload(@Named(RETROFIT_TOKOCHAT_DOWNLOAD_IMAGE) retrofit: Retrofit): TokoChatDownloadImageApi {
        return retrofit.create(TokoChatDownloadImageApi::class.java)
    }
}
