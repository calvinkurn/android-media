package com.tokopedia.product.detail.testing

import okhttp3.*
import okio.Buffer

/**
 * Created by Yehezkiel on 29/07/21
 */
abstract class BasePdpInterceptor : Interceptor {

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