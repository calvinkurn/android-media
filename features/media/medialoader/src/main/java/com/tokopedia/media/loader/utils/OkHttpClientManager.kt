package com.tokopedia.media.loader.utils

import com.tokopedia.media.loader.data.DEFAULT_TIMEOUT
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientManager {
    private var baseClient: OkHttpClient? = null
    private var clientList: MutableMap<Int, OkHttpClient> = mutableMapOf()

    private const val clientListLimit = 5

    fun getClient(timeout: Int): OkHttpClient {
        return clientList[timeout] ?: run {
            if (timeout == DEFAULT_TIMEOUT) {
                baseClient ?: initBase()
            } else {
                createClient(timeout)
            }
        }
    }

    private fun createClient(timeout: Int): OkHttpClient {
        if (clientList.size >= clientListLimit) clientList.clear()
        return OkHttpClient.Builder().apply {
            val timeoutL = timeout.toLong()
            connectTimeout(timeoutL, TimeUnit.MILLISECONDS)
            callTimeout(timeoutL, TimeUnit.MILLISECONDS)
            writeTimeout(timeoutL, TimeUnit.MILLISECONDS)
            readTimeout(timeoutL, TimeUnit.MILLISECONDS)
        }.build().also {
            clientList[timeout] = it
        }
    }

    private fun initBase(): OkHttpClient {
        return OkHttpClient().also {
            baseClient = it
        }
    }
}
