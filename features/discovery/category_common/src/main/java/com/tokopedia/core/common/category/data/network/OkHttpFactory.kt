package com.tokopedia.core.common.category.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpFactory(val context: Context) {

    private val client = getDefaultClient()
    private var builder: OkHttpClient.Builder? = null

    init {
        builder = client.newBuilder()
    }

    companion object {
        @JvmStatic
        fun create(context: Context): OkHttpFactory {
            return OkHttpFactory(context)
        }
    }

    private fun getDefaultClient(): OkHttpClient {
        return getDefaultClientConfig(OkHttpClient.Builder())
                .build()
    }

    private fun getDefaultClientConfig(builder: OkHttpClient.Builder): TkpdOkHttpBuilder {
        return TkpdOkHttpBuilder(context, builder).addInterceptor(getHttpLoggingInterceptor())
    }

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        var loggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY
        }
        return HttpLoggingInterceptor().setLevel(loggingLevel)
    }

    fun buildDaggerClientNoAuthWithBearer(
            bearerWithAuthInterceptor: TopAdsAuthInterceptor,
            fingerprintInterceptor: FingerprintInterceptor,
            tkpdBaseInterceptor: TkpdBaseInterceptor,
            chuckInterceptor: ChuckerInterceptor,
            debugInterceptor: DebugInterceptor): OkHttpClient {

        val tkpdbBuilder = TkpdOkHttpBuilder(context, builder)
                .addInterceptor(bearerWithAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .setOkHttpRetryPolicy()

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor)
            tkpdbBuilder.addInterceptor(chuckInterceptor)
        }
        return tkpdbBuilder.build()
    }
}