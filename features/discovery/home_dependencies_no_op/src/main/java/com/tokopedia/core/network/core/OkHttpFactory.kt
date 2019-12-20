package com.tokopedia.core.network.core

import com.tokopedia.core.util.GlobalConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpFactory {

    private val client = getDefaultClient()
    protected var okHttpRetryPolicy: OkHttpRetryPolicy? = null
    protected var builder: OkHttpClient.Builder

    init {
        builder = client.newBuilder()
    }

    companion object {
        @JvmStatic
        fun create(): OkHttpFactory {
            return OkHttpFactory()
        }
    }

    private fun getDefaultClient(): OkHttpClient {
        return getDefaultClientConfig(OkHttpClient.Builder())
                .build()
    }

    private fun getDefaultClientConfig(builder: OkHttpClient.Builder): TkpdOkHttpBuilder {
        return TkpdOkHttpBuilder(builder).addInterceptor(getHttpLoggingInterceptor())
    }

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        var loggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY
        }
        return HttpLoggingInterceptor().setLevel(loggingLevel)
    }

    fun addOkHttpRetryPolicy(okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpFactory {
        this.okHttpRetryPolicy = okHttpRetryPolicy
        return this
    }

    fun buildClientDefaultAuth(): OkHttpClient {
        return buildClientDefaultAuthBuilder().build()
    }

    fun buildClientDefaultAuthBuilder(): TkpdOkHttpBuilder {
        return TkpdOkHttpBuilder(builder)
                .addDebugInterceptor()
    }
}