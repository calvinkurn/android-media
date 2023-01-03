package com.tokopedia.dev_monitoring_tools.leakpublisher

import com.gojek.leak.publisher.BuildConfig
import com.gojek.leak.publisher.LeakNetworkRegistrar
import com.tokopedia.config.GlobalConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokoLeakNetworkRegistrar : LeakNetworkRegistrar {

    private val KIBANA_SECRET_HEADER = "X-App-Group-Secret"
    private val KIBANA_APP_NAME_HEADER = "X-App-Name"
    private val KIBANA_BASE_URL = "https://barito-router.golabs.io/"
    private val KIBANA_APP_NAME = "tokopedia_android_consumer_app_leaks"

    override fun factory(baseUrl: String, converters: List<Converter.Factory>): Retrofit {
        val headers = mapOf(
            KIBANA_SECRET_HEADER to BuildConfig.KIBANA_SECRET,
            KIBANA_APP_NAME_HEADER to KIBANA_APP_NAME
        )
        val client = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor())
            .addInterceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder()
                headers.forEach {
                    val (k, v) = it
                    builder.header(k, v)
                }
                chain.proceed(builder.build())
            }.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(KIBANA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    companion object {
        @Volatile
        private var INSTANCE: TokoLeakNetworkRegistrar? = null

        fun getInstance(): TokoLeakNetworkRegistrar {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokoLeakNetworkRegistrar().also { INSTANCE = it }
            }
        }
    }
}
