package com.tokopedia.fakeresponse.data.interceptor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.fakeresponse.chuck.Utils
import com.tokopedia.fakeresponse.data.parsers.bodyParser.GqlRequestBodyParser
import com.tokopedia.fakeresponse.data.parsers.bodyParser.RestBodyParser
import com.tokopedia.fakeresponse.db.AppDatabase
import okhttp3.*
import okio.Buffer

class GqlTestingInterceptor(context: Context) : Interceptor {

    private val gqlDao = AppDatabase.getDatabase(context).gqlDao()
    private val gqlRequestParser = GqlRequestBodyParser(gqlDao)

    private val restDao = AppDatabase.getDatabase(context).restDao()
    private val restRequestParser = RestBodyParser(restDao)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val request = chain.request()
            val buffer = Buffer()
            request.body()?.writeTo(buffer)
            val requestBody = buffer.readUtf8()
            if (isGqlRequest(requestBody)) {
                if (!TextUtils.isEmpty(requestBody)) {
                    val fakeResponse = gqlRequestParser.parse(requestBody)
                    if (!fakeResponse.isNullOrEmpty()) {
                        return createResponseFromFakeResponse(fakeResponse, request)
                    }
                }
            } else {
                val fakeResponse = restRequestParser.getFakeResponse(request.url(), request.method())
                if (!fakeResponse.isNullOrEmpty()) {
                    return createResponseFromFakeResponse(fakeResponse, request)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    fun createResponseFromFakeResponse(fakeResponse: String, request: Request): Response {
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        return Response.Builder()
                .code(200)
                .message(fakeResponse)
                .request(request)
                .body(ResponseBody.create(mediaType, fakeResponse))
                .protocol(Protocol.HTTP_2)
                .build()
    }

    fun isGqlRequest(requestBody: String?): Boolean {
        return Utils.isGqlRequest(requestBody)
    }

}