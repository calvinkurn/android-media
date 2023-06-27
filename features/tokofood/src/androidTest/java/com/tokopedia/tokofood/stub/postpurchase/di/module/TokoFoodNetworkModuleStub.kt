package com.tokopedia.tokofood.stub.postpurchase.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokochat.tokochat_config_common.repository.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.data.model.response.TkpdV4ResponseError
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object TokoFoodNetworkModuleStub {

    private const val NET_READ_TIMEOUT = 1
    private const val NET_WRITE_TIMEOUT = 1
    private const val NET_CONNECT_TIMEOUT = 1
    private const val NET_RETRY = 1

    private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(GSON_DATE_FORMAT)
            .setLenient()
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    @Provides
    @TokoFoodOrderTrackingScope
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
    @TokoFoodOrderTrackingScope
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideOkHttpClient(
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

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideChatRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder
            .baseUrl("http://localhost:8090/") // local base url
            .addConverterFactory(StringResponseConverter())
            .client(okHttpClient).build()
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }
}
