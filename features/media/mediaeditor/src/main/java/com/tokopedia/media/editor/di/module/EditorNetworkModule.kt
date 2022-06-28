package com.tokopedia.media.editor.di.module

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.di.EditorQualifier
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
object EditorNetworkModule {

    private const val BASE_URL = "https://mu.tokopedia.com/"
    private const val DEFAULT_TIMEOUT = 60

    @Provides
    @EditorQualifier
    fun provideOkHttpClientBuilder(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .also {
                (context as? NetworkRouter?)?.let { router ->
                    it.addInterceptor(FingerprintInterceptor(router, userSession))
                    it.addInterceptor(TkpdAuthInterceptor(context, router, userSession))
                }
            }
    }

    @Provides
    @EditorQualifier
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

}