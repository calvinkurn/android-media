package com.rahullohra.fakeresponse.data.interceptor

import android.content.Context
import android.text.TextUtils
import com.rahullohra.fakeresponse.data.parsers.bodyParser.RestBodyParser
import com.rahullohra.fakeresponse.db.AppDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer

class RestInterceptor(context: Context) : Interceptor {
    val dao = AppDatabase.getDatabase(context).restDao()
    val requestParser = RestBodyParser(dao)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val request = chain.request()
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            val fakeResponse = requestParser.getFakeResponse(request.url, request.method)
            if (!TextUtils.isEmpty(fakeResponse)) {
                return createResponseFromFakeResponse(fakeResponse!!, request)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    fun createResponseFromFakeResponse(fakeResponse: String, request: Request): Response {
        val formattedResponse = fakeResponse
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        return Response.Builder()
            .code(200)
            .message(fakeResponse)
            .request(request)
            .body(ResponseBody.create(mediaType, formattedResponse))
            .protocol(Protocol.HTTP_1_1)
            .build()
    }


}