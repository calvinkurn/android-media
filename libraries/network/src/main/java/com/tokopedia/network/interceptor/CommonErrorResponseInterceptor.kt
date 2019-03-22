package com.tokopedia.network.interceptor

import com.google.gson.JsonArray
import com.tokopedia.network.exception.MessageErrorException
import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

/**
 * Generic ErrorResponseInterceptor to handle common error response
 * will search for the key "error", "message_error" in the body
 * and will return as MessageErrorException()
 */

class CommonErrorResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val response = chain.proceed(chain.request())

        val responseBody: ResponseBody?
        var responseBodyString = ""
        if (response != null) {
            responseBody = response.peekBody(BYTE_COUNT.toLong())
            responseBodyString = responseBody!!.string()

            val jsonObject = JSONObject(responseBodyString)
            var errorKey: String = ""
            for (errItem in errorKeyList) {
                if (jsonObject.has(errItem)) {
                    errorKey = errItem
                    break;
                }
            }
            if (errorKey.isNotEmpty()) {
                var errorStringArray: JSONArray? = null
                try {
                    errorStringArray = jsonObject.optJSONArray(errorKey)
                } catch (e: Exception) {

                }
                if (errorStringArray == null) {
                    val errorMessage = jsonObject.optString(errorKey)
                    if (errorMessage.isNotEmpty()) {
                        throw MessageErrorException(errorMessage)
                    }
                } else {
                    val listdata = mutableListOf<String?>()
                    var i = 0
                    val size = errorStringArray.length()
                    while (i < size) {
                        listdata.add(errorStringArray[i].toString())
                        i++
                    }
                    val errorMessageJoin = listdata.mapNotNull { it }
                            .joinToString(separator = ", ")
                    if (errorMessageJoin.isNotEmpty()) {
                        throw MessageErrorException(errorMessageJoin)
                    }
                }
            }
        }
        return response
    }

    companion object {
        private val BYTE_COUNT = 2048
        private val errorKeyList by lazy {
            mutableListOf("error", "message_error", "message_errors", "errors")
        }
    }

}
