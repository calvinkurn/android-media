package com.rahullohra.fakeresponse.data.interceptor

import android.content.Context
import android.text.TextUtils
import com.rahullohra.fakeresponse.data.parsers.bodyParser.GqlRequestBodyParser
import com.rahullohra.fakeresponse.data.parsers.SupportedQueryName
import com.rahullohra.fakeresponse.db.AppDatabase
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class GqlTestingInterceptor(context: Context) : Interceptor {

    val dao = AppDatabase.getDatabase(context).gqlDao()
    val requestParser = GqlRequestBodyParser(dao)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val request = chain.request()
            val buffer = Buffer()
            request.body()?.writeTo(buffer)
            val requestBody = buffer.readUtf8()
            if (isGqlRequest(request)) {
                if (!TextUtils.isEmpty(requestBody)) {
                    val fakeResponse = requestParser.parse(requestBody)
                    if (!TextUtils.isEmpty(fakeResponse)) {
                        return createResponseFromFakeResponse(fakeResponse!!, request)
                    }
                }
            }
        }catch (e:Exception){
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

    fun isGqlRequest(request: Request): Boolean {
        return true
    }

}