package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.util.MediaUploaderNetwork
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideOkHttpClientBuilder(
        @ApplicationContext context: Context,
        @MediaUploaderQualifier userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(NetworkTimeOutInterceptor())
            .also {
                // adding tkpdAuth and fingerprint interceptor
                (context as? NetworkRouter?)?.let { router ->
                    it.addInterceptor(FingerprintInterceptor(router, userSession))
                    it.addInterceptor(TkpdAuthInterceptor(context, router, userSession))
                }

                if (GlobalConfig.isAllowDebuggingTools()) {
                    it.addInterceptor(ChuckerInterceptor(context))
                }
            }
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderRetrofitBuilder(): Retrofit.Builder {
        return MediaUploaderNetwork.retrofitBuilder()
    }

    companion object {
        private const val BASE_URL = "https://upedia.tokopedia.net/"
    }

}