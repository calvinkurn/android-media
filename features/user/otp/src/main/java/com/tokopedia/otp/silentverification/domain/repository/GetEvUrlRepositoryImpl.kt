package com.tokopedia.otp.silentverification.domain.repository

import android.net.Network
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Yoris on 02/11/21.
 */

class GetEvUrlRepositoryImpl(private val okHttpBuilder: OkHttpClient.Builder): GetEvUrlRepository {
    override fun getEvUrl(network: Network, url: String): Call {
        val client = okHttpBuilder
            .socketFactory(network.socketFactory)
            .build()
        val request = Request.Builder().url(url).build()
        return client.newCall(request)
    }
}