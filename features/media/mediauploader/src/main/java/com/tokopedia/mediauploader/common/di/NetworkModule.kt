package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.mediauploader.common.util.interceptor.NetworkTimeOutInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @UploaderQualifier
    fun provideOkHttpClientBuilder(
        @ApplicationContext context: Context,
        @UploaderQualifier userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkTimeOutInterceptor.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(NetworkTimeOutInterceptor.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NetworkTimeOutInterceptor.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .callTimeout(NetworkTimeOutInterceptor.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(NetworkTimeOutInterceptor())
            .also {
                (context as? NetworkRouter?)?.let { router ->
                    it.addInterceptor(FingerprintInterceptor(router, userSession))
                    it.addInterceptor(TkpdAuthInterceptor(context, router, userSession))
                }

                if (GlobalConfig.isAllowDebuggingTools()) {
                    it.addInterceptor(
                        ChuckerInterceptor(
                        context = context,
                        maxContentLength = MAX_LENGTH_CHUCKER_CONTENT
                    )
                    )
                }
            }
    }

    @Provides
    @UploaderQualifier
    fun provideMediaUploaderRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
    }

    companion object {
        val BASE_URL = TokopediaUrl.getInstance().WEB
        private const val MAX_LENGTH_CHUCKER_CONTENT = 1000L
    }

}
