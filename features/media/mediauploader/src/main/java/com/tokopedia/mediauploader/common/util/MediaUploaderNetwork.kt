package com.tokopedia.mediauploader.common.util

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.DEFAULT_TIMEOUT
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MediaUploaderNetwork {

    const val BASE_URL = "https://upedia.tokopedia.net/"
    private const val MAX_LENGTH_CHUCKER_CONTENT = 1000L

    fun okHttpClientBuilder(
        context: Context,
        userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(NetworkTimeOutInterceptor())
            .also {
                (context as? NetworkRouter?)?.let { router ->
                    it.addInterceptor(FingerprintInterceptor(router, userSession))
                    it.addInterceptor(TkpdAuthInterceptor(context, router, userSession))
                }

                if (GlobalConfig.isAllowDebuggingTools()) {
                    it.addInterceptor(ChuckerInterceptor(
                        context = context,
                        maxContentLength = MAX_LENGTH_CHUCKER_CONTENT
                    ))
                }
            }
    }

    fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
    }

}