package com.tokopedia.iris.data.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.tokopedia.iris.BASE_URL
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import okhttp3.OkHttpClient


/**
 * Created by meta on 21/11/18.
 */
class ApiService(private val context: Context) {

    fun makeRetrofitService(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(OkHttpBuilder(context, OkHttpClient.Builder()).build())
                .build()
    }

    companion object {

        fun parse(data: String) : RequestBody {
            val jsonObject = JSONObject(data).toString()
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    jsonObject)
        }
    }
}
