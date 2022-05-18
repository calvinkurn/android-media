package com.tokopedia.imagepicker.editor.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepicker.editor.di.scope.ImageEditorQualifier
import com.tokopedia.imagepicker.editor.di.scope.ImageEditorScope
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @ImageEditorScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ImageEditorQualifier
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

                if (GlobalConfig.isAllowDebuggingTools()) {
                    it.addInterceptor(ChuckerInterceptor(
                        context = context,
                        maxContentLength = 1000L
                    ))
                }
            }
    }

    @Provides
    @ImageEditorQualifier
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
    }

    companion object {
        private const val BASE_URL = "https://mu.tokopedia.com/"
        private const val DEFAULT_TIMEOUT = 60
    }

}