package com.tokopedia.oneclickcheckout.common.interceptor

import okhttp3.*
import okio.Buffer

abstract class BaseOccInterceptor : Interceptor {

    fun readRequestString(copyRequest: Request): String {
        val buffer = Buffer()
        copyRequest.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }

    fun mockResponse(copy: Request, responseString: String): Response {
        return Response.Builder()
                .request(copy)
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(ResponseBody.create(MediaType.parse("application/json"),
                        responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
    }

    abstract fun resetInterceptor()
}