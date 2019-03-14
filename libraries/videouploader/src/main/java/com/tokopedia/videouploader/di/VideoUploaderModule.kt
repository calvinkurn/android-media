package com.tokopedia.videouploader.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.ProgressResponseBody
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videouploader.data.UploadVideoApi
import com.tokopedia.videouploader.data.UploadVideoUrl
import com.tokopedia.videouploader.data.VideoUploaderResponseError
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author by nisie on 14/03/19.
 */

@Module
class VideoUploaderModule(private val isNeedProgress: Boolean = false,
                          private val progressListener: ProgressResponseBody.ProgressListener) {

    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 1

    @VideoUploaderQualifier
    @Provides
    fun provideOkHttpClient(@VideoUploaderQualifier tkpdAuthInterceptor: TkpdAuthInterceptor,
                            @VideoUploaderQualifier fingerprintInterceptor: FingerprintInterceptor,
                            @VideoUploaderQualifier retryPolicy: OkHttpRetryPolicy,
                            @VideoUploaderQualifier chuckInterceptor: Interceptor,
                            @VideoUploaderQualifier loggingInterceptor: HttpLoggingInterceptor,
                            @VideoUploaderQualifier errorHandlerInterceptor: ErrorResponseInterceptor,
                            @VideoUploaderQualifier cacheApiInterceptor: CacheApiInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(cacheApiInterceptor)
        builder.addInterceptor(errorHandlerInterceptor)

        if (isNeedProgress) {
            builder.addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                        .body(ProgressResponseBody(originalResponse.body(), progressListener))
                        .build()
            }
        }

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(chuckInterceptor)
        }

        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @VideoUploaderQualifier
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context,
                             networkRouter: NetworkRouter,
                             userSessionInterface: UserSessionInterface): NetworkRouter {
        return (context as NetworkRouter)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @VideoUploaderQualifier
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @VideoUploaderQualifier
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(VideoUploaderResponseError::class.java)
    }

    @VideoUploaderQualifier
    @Provides
    fun provideCacheApiInterceptor(): CacheApiInterceptor {
        return CacheApiInterceptor()
    }

    @VideoUploaderQualifier
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @VideoUploaderQualifier
    @Provides
    fun provideRetrofitBuilder(@VideoUploaderQualifier gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @VideoUploaderQualifier
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(@VideoUploaderQualifier okHttpClient: OkHttpClient,
                                            @VideoUploaderQualifier retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(UploadVideoUrl.BASE_URL).client(okHttpClient).build()
    }

    @VideoUploaderQualifier
    @Provides
    fun provideUploadVideoApi(@VideoUploaderQualifier retrofit: Retrofit): UploadVideoApi {
        return retrofit.create(UploadVideoApi::class.java)
    }
}