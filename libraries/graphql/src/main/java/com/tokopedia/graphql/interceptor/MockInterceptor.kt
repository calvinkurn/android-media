package com.tokopedia.graphql.interceptor

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mock_response")

@Keep
class MockInterceptor(val applicationContext: Context) : Interceptor {

    fun getMockResponse(gqlKey: String): String {
        val value = runBlocking {
            val mockResponse = applicationContext?.dataStore?.data?.let {
                it.map { preferences ->
                    // No type safety.
                    preferences[stringPreferencesKey(gqlKey)] ?: ""
                }.first()
            }
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
