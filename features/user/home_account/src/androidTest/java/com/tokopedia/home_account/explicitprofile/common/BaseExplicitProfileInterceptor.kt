package com.tokopedia.home_account.explicitprofile.common

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

abstract class BaseExplicitProfileInterceptor: Interceptor {

    fun mockResponse(request: Request, mockResponse: String, networkCode: Int = 200): Response {
        return Response.Builder()
            .request(request)
            .code(networkCode)
            .protocol(Protocol.HTTP_2)
            .message(mockResponse)
            .body(mockResponse.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}