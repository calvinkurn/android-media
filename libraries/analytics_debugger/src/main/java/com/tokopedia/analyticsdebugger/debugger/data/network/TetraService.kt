package com.tokopedia.analyticsdebugger.debugger.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.config.GlobalConfig
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 2019-08-16.
 */
class TetraService(private val context: Context) {

    private var tetraApi: TetraApi? = null

    fun makeRetrofitService(): TetraApi {
        if (tetraApi == null)
            tetraApi = Retrofit.Builder()
                .baseUrl("http://172.31.2.144/")
                .addConverterFactory(StringResponseConverter())
                .client(createClient())
                .build().create(TetraApi::class.java)
        return tetraApi!!
    }

    private fun createClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(ChuckerInterceptor(context))
        }
        return builder.build()
    }

    companion object {

        fun parse(data: String) : RequestBody {
            val jsonObject = JSONObject(data).toString()
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    jsonObject)
        }
    }
}