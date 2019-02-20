package com.tokopedia.iris.data.network

import android.content.Context

import com.readystatesoftware.chuck.ChuckInterceptor

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpBuilder(private val context: Context, val builder: OkHttpClient.Builder) {

    private fun addInterceptor(interceptor: Interceptor): OkHttpBuilder {
        builder.addInterceptor(interceptor)
        return this
    }

    private fun addDebugInterceptor(): OkHttpBuilder {
        if (BuildConfig.DEBUG) {
            this.addInterceptor(ChuckInterceptor(context))
        }

        return this
    }

    private fun setOkHttpRetryPolicy(): OkHttpBuilder {
        builder.readTimeout(45, TimeUnit.SECONDS)
        builder.connectTimeout(45, TimeUnit.SECONDS)
        builder.writeTimeout(45, TimeUnit.SECONDS)

        return this
    }

    fun addLegacyChiper(): OkHttpBuilder {
        // Add legacy cipher suite for Android 4
        var cipherSuites: MutableList<CipherSuite>? = ConnectionSpec.MODERN_TLS.cipherSuites()
        if (!cipherSuites!!.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = ArrayList(cipherSuites)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA)
        }
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(*cipherSuites.toTypedArray())
                .build()
        builder.connectionSpecs(listOf(spec))
        return this
    }

    fun build(): OkHttpClient {
        setOkHttpRetryPolicy()
        addDebugInterceptor()
        addLegacyChiper()
        return builder.build()
    }
}
