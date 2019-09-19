package com.tokopedia.iris.data.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.*
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by meta on 21/11/18.
 */
class ApiService(private val context: Context) {

    private val session: Session = IrisSession(context)
    private val userSession: UserSessionInterface = UserSession(context)
    private var apiInterface: ApiInterface? = null

    fun makeRetrofitService(): ApiInterface {
        if (apiInterface == null)
            apiInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(createClient())
                .build().create(ApiInterface::class.java)
        return apiInterface!!
    }

    private fun createClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                    request.header(HEADER_CONTENT_TYPE, HEADER_JSON)
                    if (!session.getUserId().isBlank()) {
                        request.header(HEADER_USER_ID, session.getUserId())
                    }
                    request.header(HEADER_DEVICE, HEADER_ANDROID)
                    request.method(original.method(), original.body())
                    val requestBuilder = request.build()

                    it.proceed(requestBuilder)
                }
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
        addFringerInterceptor(builder)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(ChuckInterceptor(context))
        }
        return builder.build()
    }

    private fun addFringerInterceptor(builder:OkHttpClient.Builder){
        builder.addInterceptor(FingerprintInterceptor(context.applicationContext as NetworkRouter, userSession))
    }

    companion object {

        fun parse(data: String) : RequestBody {
            val jsonObject = JSONObject(data).toString()
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    jsonObject)
        }
    }
}
