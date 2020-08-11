package com.tokopedia.test.application.environment.interceptor.mock

import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_QUERY_NAME
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MockInterceptor(val responseConfig: MockModelConfig) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val copy = chain.request().newBuilder().build()
                val buffer = Buffer()
                copy.body()?.writeTo(buffer)
                val requestString = buffer.readUtf8()

                var responseString = ""
                responseConfig.getResponseList().forEach {
                    if (it.value.findType == FIND_BY_CONTAINS) {
                        if (requestString.contains(it.key)) {
                            responseString = it.value.value
                            return mockResponse(copy, responseString)
                        }
                    } else if (it.value.findType == FIND_BY_QUERY_NAME) {
                        val requestCopy = chain.request().newBuilder().build()
                        val buffer = Buffer()
                        requestCopy.body()?.writeTo(buffer)
                        val requestString = buffer.readUtf8()
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
                            return mockResponse(copy, responseString)
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
                .body(ResponseBody.create(MediaType.parse("application/json"),
                        responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
    }
}