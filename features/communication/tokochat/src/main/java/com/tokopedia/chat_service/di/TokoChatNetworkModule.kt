package com.tokopedia.chat_service.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.chat_service.data.interceptor.GojekInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
object TokoChatNetworkModule {

    //TODO: Move this to TokopediaUrl
    private const val BASE_URL = "https://integration-api.gojekapi.com/"

    private const val NET_READ_TIMEOUT = 300
    private const val NET_WRITE_TIMEOUT = 300
    private const val NET_CONNECT_TIMEOUT = 300
    private const val NET_RETRY = 3

    const val RETROFIT_NAME = "retrofit_chatservice"

    @TokoChatScope
    @Provides
    @Named(RETROFIT_NAME)
    fun provideChatRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .addConverterFactory(StringResponseConverter())
            .client(okHttpClient).build()
    }

    @TokoChatScope
    @Provides
    fun provideOkHttpClient(
        retryPolicy: OkHttpRetryPolicy,
        loggingInterceptor: HttpLoggingInterceptor,
        errorResponseInterceptor: ErrorResponseInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        gojekInterceptor: GojekInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorResponseInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }

        builder.addInterceptor(gojekInterceptor)

        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @TokoChatScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @TokoChatScope
    @Provides
    fun provideGojekInterceptor(): GojekInterceptor {
        return GojekInterceptor()
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
}
