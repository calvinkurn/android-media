package com.tokopedia.graphql.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author by furqan on 13/07/2021
 */
class GraphqlEmbraceInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()

        val gqlQueryName: String = request.headers().get("x-tkpd-clc")?.split("-")?.get(0) ?: ""
        if (gqlQueryName.isNotEmpty()) {
            val embracePath = "/$gqlQueryName"
            newRequestBuilder.header("x-emb-path", embracePath)
        }

        val newRequest: Request = newRequestBuilder.build()
        return chain.proceed(newRequest)
    }

}