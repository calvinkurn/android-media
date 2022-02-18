package com.tokopedia.kyc_centralized.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.data.StringResponseConverter
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.kyc_centralized.KycUrl
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
open class KycUploadImageModule {
    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 300
    private val NET_WRITE_TIMEOUT = 300
    private val NET_CONNECT_TIMEOUT = 300
    private val NET_RETRY = 3

    @ActivityScope
    @KycQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    @ActivityScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @ActivityScope
    @Provides
    open fun provideApi(okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): KycUploadApi {
        val retrofit = retrofitBuilder.baseUrl(KycUrl.getKYCBaseUrl()).client(okHttpClient).build()
        return retrofit.create(KycUploadApi::class.java)
    }

    @ActivityScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @ActivityScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ActivityScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ActivityScope
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            retryPolicy: OkHttpRetryPolicy,
                            loggingInterceptor: HttpLoggingInterceptor,
                            errorHandlerInterceptor: ErrorResponseInterceptor,
                            chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorHandlerInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(AkamaiBotInterceptor(context))

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

    @ActivityScope
    @KycQualifier
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @ActivityScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

}