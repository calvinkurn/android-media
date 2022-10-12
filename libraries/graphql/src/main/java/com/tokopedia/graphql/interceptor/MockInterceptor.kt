package com.tokopedia.graphql.interceptor

import android.content.Context
import androidx.annotation.Keep
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

@Keep
class MockInterceptor(val applicationContext: Context) : Interceptor {
    val sharedPref = applicationContext.getSharedPreferences(
        "mock_response", Context.MODE_PRIVATE)

    fun getMockResponse(gqlKey: String): String {
        val value = runBlocking {
            val mockResponse = sharedPref.getString(gqlKey, "")
            return@runBlocking mockResponse
        }
        return value?:""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val requestBody = chain.request()
            val buffer = Buffer()
            requestBody.body?.writeTo(buffer)
            val requestString = buffer.readUtf8()

            var responseString = ""

            if (requestString.isNotEmpty()) {
                val requestArray = JSONArray(requestString)
                val requestObject: JSONObject = requestArray.getJSONObject(0)
                val queryString = requestObject.getString("query")
                val queryStringCopy = queryString.removePrefix("query ")
                    .removePrefix("{\n ")
                    .removePrefix(" ")

                var firstWord = queryStringCopy
                if (firstWord.contains("\n")) {
                    firstWord = firstWord
                        .substringBefore("\n", "")
                }
                if (firstWord.contains(" ")) {
                    firstWord = firstWord.substringBefore(" ", "")
                }
                if (firstWord.contains("(")) {
                    firstWord = firstWord.substringBefore("(", "")
                }

                responseString = getMockResponse(firstWord)
                return if (responseString.isNotEmpty()) {
                    mockResponse(requestBody.newBuilder().build(), responseString)
                } else {
                    chain.proceed(chain.request())
                }
            }
        } catch (e: IOException) {
            return chain.proceed(chain.request())
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
