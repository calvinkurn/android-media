package com.tokopedia.checkout.interceptor

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.test.application.util.InstrumentationMockHelper
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

abstract class BaseCheckoutInterceptor(val context: Context) : Interceptor {

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
                responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    fun getRawString(@RawRes resourceId: Int): String {
        return InstrumentationMockHelper.getRawString(context, resourceId)
    }

    abstract fun resetInterceptor()
}
