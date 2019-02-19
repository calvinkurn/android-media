package com.tokopedia.iris.data.network

import android.content.Context
import android.os.Build
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.iris.*
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion
import okhttp3.OkHttpClient
import javax.net.ssl.SSLContext


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
                 .followRedirects(true)
                 .followSslRedirects(true)
                 .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckInterceptor(context))
        }

        return enableTls12OnPreLollipop(builder).build()
    }

    fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.getSocketFactory()))

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = ArrayList<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                // Error while setting TLS 1.2
            }

        }

        return client
    }
    companion object {

        fun parse(data: String) : RequestBody {
            val jsonObject = JSONObject(data).toString()
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    jsonObject)
        }
    }
}
