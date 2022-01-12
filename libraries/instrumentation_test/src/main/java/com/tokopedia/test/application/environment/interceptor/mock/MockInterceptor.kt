package com.tokopedia.test.application.environment.interceptor.mock

import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_QUERY_NAME
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MockInterceptor(val responseConfig: MockModelConfig) : Interceptor {

    companion object {
        const val KEY = "MockInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val requestBody = chain.request()
                val buffer = Buffer()
                requestBody.body?.writeTo(buffer)
                val requestString = buffer.readUtf8()

                var responseString = ""
                responseConfig.getResponseList().forEach {
                    if (it.value.findType == FIND_BY_CONTAINS) {
                        if (requestString.contains(it.key)) {
                            responseString = it.value.value
                            return mockResponse(requestBody.newBuilder().build(), responseString)
                        }
                    } else if (it.value.findType == FIND_BY_QUERY_NAME) {
                        val requestArray = JSONArray(requestString)
                        val requestObject: JSONObject = requestArray.getJSONObject(0)
                        val queryString = requestObject.getString("query")
                        val queryStringCopy = queryString.removePrefix("query ")
                            .removePrefix("{\n ")
                            .removePrefix(" ")
                        val firstWord =
                            queryStringCopy.substringBefore(" ", "")
                                .substringBefore("\n", "")
                        if (firstWord == it.key) {
                            responseString = it.value.value
                            return mockResponse(requestBody.newBuilder().build(), responseString)
                        }
                    }
                }
            } catch (e: IOException) {
                "did not work"
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }

        return chain.proceed(chain.request())
    }

    private fun mockResponse(copy: Request, responseString: String): Response {
        return Response.Builder()
                .request(copy)
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    responseString.toByteArray()
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
                .addHeader("content-type", "application/json")
                .build()
    }
}