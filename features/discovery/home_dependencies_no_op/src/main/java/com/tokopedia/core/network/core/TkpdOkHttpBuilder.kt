package com.tokopedia.core.network.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class TkpdOkHttpBuilder {

    private var builder: OkHttpClient.Builder

    constructor(builder: OkHttpClient.Builder) {
        this.builder = builder
    }

    fun addInterceptor(interceptor: Interceptor): TkpdOkHttpBuilder {
        builder.addInterceptor(interceptor)
        return this
    }

    fun addDebugInterceptor(): TkpdOkHttpBuilder {
        return this
    }

    fun build(): OkHttpClient {
        return builder.build()
    }
}