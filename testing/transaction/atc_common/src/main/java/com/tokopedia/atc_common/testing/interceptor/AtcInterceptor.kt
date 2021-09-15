package com.tokopedia.atc_common.testing.interceptor

import android.content.Context
import com.tokopedia.atc_common.testing.R
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import okhttp3.*
import okio.Buffer

open class AtcInterceptor(private val context: Context): Interceptor {

    var customAtcThrowable: Throwable? = null
    var customAtcResponseResource: Int? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(ATC_V2_QUERY) || requestString.contains(ATC_OCC_MULTI_QUERY)) {
            if (customAtcThrowable != null) {
                throw customAtcThrowable!!
            } else if (customAtcResponseResource != null) {
                return mockResponse(copy, getRawString(context, customAtcResponseResource!!))
            }
            val response = when {
                requestString.contains(ATC_V2_QUERY) -> ATC_V2_SUCCESS_RESPONSE
                requestString.contains(ATC_OCC_MULTI_QUERY) -> ATC_OCC_MULTI_SUCCESS_RESPONSE
                else -> null
            }
            if (response != null) {
                return mockResponse(copy, getRawString(context, response))
            }
        }
        return chain.proceed(chain.request())
    }

    private fun readRequestString(copyRequest: Request): String {
        val buffer = Buffer()
        copyRequest.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }

    private fun mockResponse(copy: Request, responseString: String): Response {
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

    companion object {
        const val ATC_V2_QUERY = "add_to_cart_v2"
        const val ATC_OCC_MULTI_QUERY = "add_to_cart_occ_multi"

        val ATC_V2_SUCCESS_RESPONSE = R.raw.add_to_cart_v2_success_response
        val ATC_V2_ERROR_RESPONSE = R.raw.add_to_cart_v2_error_response

        val ATC_OCC_MULTI_SUCCESS_RESPONSE = R.raw.add_to_cart_occ_multi_success_response
        val ATC_OCC_MULTI_ERROR_RESPONSE = R.raw.add_to_cart_occ_multi_error_response
    }
}