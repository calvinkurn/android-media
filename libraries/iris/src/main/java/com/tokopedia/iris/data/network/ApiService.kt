package com.tokopedia.iris.data.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.iris.*
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient


/**
 * Created by meta on 21/11/18.
 */
class ApiService(private val context: Context) {

    fun makeRetrofitService(): ApiInterface {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createClient())
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(ApiInterface::class.java)
    }

    private fun createClient(): OkHttpClient {
         val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS)
        builder.readTimeout(10000, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckInterceptor(context))
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
