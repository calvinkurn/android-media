package com.tokopedia.managepassword.common.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.managepassword.di.ManagePasswordContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 */

class ManagePasswordApiClient<T> @Inject constructor(
        @ManagePasswordContext
        private val context: Context,
        private val service: Class<T>
) {
    private val userSession: UserSessionInterface = UserSession(context)
    private val fingerprintInterceptor: FingerprintInterceptor = FingerprintInterceptor(context as NetworkRouter, userSession)
    private val tkpdAuthInterceptor: TkpdAuthInterceptor = TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    private val gson: Gson = GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .setPrettyPrinting()
            .serializeNulls()
            .create()

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(fingerprintInterceptor)
            addInterceptor(tkpdAuthInterceptor)

            if (GlobalConfig.isAllowDebuggingTools()) {
                addInterceptor(ChuckerInterceptor(context))
                addInterceptor(DebugInterceptor())
                addInterceptor(httpLoggingInterceptor)
            }
        }.build()
    }

    private fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().ACCOUNTS)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

    fun call(): T {
        return retrofitBuilder()
                .client(getClient())
                .build()
                .create(service)
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}