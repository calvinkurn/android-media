package com.tokopedia.fakeresponse.data.interceptor

import android.content.Context
import android.os.SystemClock
import android.text.TextUtils
import com.tokopedia.fakeresponse.NotificationHelper
import com.tokopedia.fakeresponse.Preference
import com.tokopedia.fakeresponse.Router
import com.tokopedia.fakeresponse.chuck.Utils
import com.tokopedia.fakeresponse.data.parsers.bodyParser.GqlRequestBodyParser
import com.tokopedia.fakeresponse.data.parsers.bodyParser.RestBodyParser
import com.tokopedia.fakeresponse.db.AppDatabase
import com.tokopedia.fakeresponse.presentation.livedata.Success
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer

class GqlTestingInterceptor(val context: Context) : Interceptor {

    companion object {
        const val DELAY_RESPONSE_TIME = 5000L
        const val ERROR_MOCK = "Error Mock"
    }

    private val gqlDao = AppDatabase.getDatabase(context).gqlDao()
    private val gqlRequestParser = GqlRequestBodyParser(gqlDao)

    private val restDao = AppDatabase.getDatabase(context).restDao()
    private val restRequestParser = RestBodyParser(restDao)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val request = chain.request()
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            val requestBody = buffer.readUtf8()
            if (isGqlRequest(requestBody)) {
                if (!TextUtils.isEmpty(requestBody)) {
                    val fakeResponse = gqlRequestParser.parse(requestBody)
                    if (fakeResponse != null) {
                        if (Preference.getIsEnableNotification(context)) {
                            NotificationHelper.show(
                                context = context,
                                fakeResponse.gqlOperationName,
                                Router.routeToAddGqlFromNotification(context, fakeResponse.id)
                            )
                        }
                        if (fakeResponse.isDelayResponse) {
                            SystemClock.sleep(DELAY_RESPONSE_TIME)
                        }
                        return createResponseFromFakeResponse(
                            fakeResponse = fakeResponse.response.orEmpty(),
                            isResponseSuccess = fakeResponse.isResponseSuccess,
                            request
                        )
                    }
                }
            } else {
                val fakeResponse = restRequestParser.getFakeResponse(request.url, request.method)
                if (!fakeResponse.isNullOrEmpty()) {
                    return createResponseFromFakeResponse(
                        fakeResponse = fakeResponse,
                        request = request
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    fun createResponseFromFakeResponse(
        fakeResponse: String,
        isResponseSuccess: Boolean = true,
        request: Request
    ): Response {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        if (isResponseSuccess) {
            return Response.Builder()
                .code(200)
                .message(fakeResponse)
                .request(request)
                .body(ResponseBody.create(mediaType, fakeResponse))
                .protocol(Protocol.HTTP_2)
                .build()
        } else {
            return Response.Builder()
                .code(500)
                .message(ERROR_MOCK)
                .request(request)
                .body(ResponseBody.create(mediaType, ERROR_MOCK))
                .protocol(Protocol.HTTP_2)
                .build()
        }
    }

    fun isGqlRequest(requestBody: String?): Boolean {
        return Utils.isGqlRequest(requestBody)
    }

}
