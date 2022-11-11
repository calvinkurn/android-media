package com.tokochat.tokochat_config_common.repository.interceptor

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.network.data.model.response.BaseResponseError
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException

class ErrorResponseInterceptor(private val responseErrorClass: Class<out BaseResponseError>) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val responseBody: ResponseBody?
        val responseBodyString: String
        if (mightContainCustomError(response)) {
            responseBody = response.peekBody(BYTE_COUNT.toLong())
            responseBodyString = responseBody.string()
            val gson = Gson()
            val responseError: BaseResponseError? = try {
                gson.fromJson(responseBodyString, responseErrorClass)
            } catch (e: JsonSyntaxException) {
                // the json might not be TkpdResponseError instance, so just return it
                return response
            }
            return if (responseError == null) { // no error object
                response
            } else {
                if (responseError.hasBody()) {
                    Timber.d(response.headers.toString())
                    Timber.d(responseBodyString)
                    response.body?.close()
                    throw responseError.createException()
                } else {
                    response
                }
            }
        }
        return response
    }

    private fun mightContainCustomError(response: Response?): Boolean {
        return response != null
    }

    companion object {
        private const val BYTE_COUNT = 2048
    }
}
