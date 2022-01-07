package com.tokopedia.inactivephone_otp.testing.common

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

abstract class BaseInactivePhoneOtpInterceptor : Interceptor {

    fun readRequestString(copyRequest: Request): String {
        val buffer = Buffer()
        copyRequest.body?.writeTo(buffer)
        return buffer.readUtf8()
    }

    fun mockResponse(copy: Request, responseString: String): Response {
        return Response.Builder()
            .request(copy)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .body(
                responseString
                    .toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}