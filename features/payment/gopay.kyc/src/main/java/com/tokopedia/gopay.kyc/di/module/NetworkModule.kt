package com.tokopedia.gopay.kyc.di.module

import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.RestConstant
import com.tokopedia.config.GlobalConfig
import com.tokopedia.gopay.kyc.di.GoPayKycScope
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
        retryPolicy: OkHttpRetryPolicy
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return clientBuilder.build()
    }

    @Provides
    @GoPayKycScope
    fun provideOkHttpTimeoutPolicy() = OkHttpRetryPolicy.createdOkHttpRetryPolicyQuickNoRetry()

    @Provides
    @GoPayKycScope
    fun provideGCSRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RestConstant.BASE_URL)
            .addConverterFactory(StringResponseConverter())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @GoPayKycScope
    fun provideRestApiWithoutHeader(retrofit: Retrofit) = retrofit.create(RestApi::class.java)
}