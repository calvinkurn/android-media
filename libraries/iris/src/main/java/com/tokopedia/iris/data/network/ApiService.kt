package com.tokopedia.iris.data.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.iris.*
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Created by meta on 21/11/18.
 */
class ApiService(private val context: Context) {

    private val session: Session = IrisSession(context)

    fun makeRetrofitService(): ApiInterface {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(createClient())
                .build().create(ApiInterface::class.java)
    }

    private fun createClient(): OkHttpClient {
         val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckInterceptor(context))
        }

        builder.connectionSpecs(listOf(legacyChiper()))
        builder.addInterceptor {
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

        return builder.build()
    }

    private fun legacyChiper() : ConnectionSpec {
        var cipherSuites: MutableList<CipherSuite>? = ConnectionSpec.MODERN_TLS.cipherSuites()
        if (!cipherSuites!!.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = ArrayList(cipherSuites)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA)
        }
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(*cipherSuites.toTypedArray())
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
