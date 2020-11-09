package com.tokopedia.iris.data.network

import android.content.Context
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.*
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by meta on 21/11/18.
 */
class ApiService(private val context: Context) {

    private val userSession: UserSessionInterface = UserSession(context)
    private var apiInterface: ApiInterface? = null
    private val DEFAULT_CONNECTION_TIMEOUT = 30000L
    private val DEFAULT_READ_TIMEOUT = 10000L
    private val DEFAULT_WRITE_TIMEOUT = 10000L
    lateinit var firebaseRemoteConfigImpl: FirebaseRemoteConfigImpl

    init {
        firebaseRemoteConfigImpl = FirebaseRemoteConfigImpl(context)
    }

    fun makeRetrofitService(): ApiInterface {
        if (apiInterface == null)
            apiInterface = Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().HUB + VERSION)
                .addConverterFactory(StringResponseConverter())
                .client(createClient())
                .build().create(ApiInterface::class.java)
        return apiInterface!!
    }

    private fun createClient(): OkHttpClient {
        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .supportsTlsExtensions(true)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                .build()
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                    request.header(HEADER_CONTENT_TYPE, HEADER_JSON)
                    val userId = userSession.userId
                    if (userId.isNotEmpty()) {
                        request.header(HEADER_USER_ID, userId)
                    }
                    request.header(HEADER_DEVICE, HEADER_ANDROID + GlobalConfig.VERSION_NAME)
                    addUserAgent(request)
                    request.method(original.method(), original.body())
                    val requestBuilder = request.build()

                    it.proceed(requestBuilder)
                }
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(irisTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
        addFringerInterceptor(builder)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(ChuckerInterceptor(context))
        }
        return builder.build()
    }

    private fun irisTimeout(): Long {
        try {
            return firebaseRemoteConfigImpl.getLong(IRIS_CUSTOM_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT)
        } catch (e: Exception){
            return DEFAULT_CONNECTION_TIMEOUT
        }
    }

    private fun addUserAgent(request: Request.Builder) {
        if (firebaseRemoteConfigImpl.getBoolean(IRIS_CUSTOM_USER_AGENT_ENABLE)) {
            request.header(USER_AGENT, getUserAgent())
        }
    }

    private fun addFringerInterceptor(builder: OkHttpClient.Builder) {
        builder.addInterceptor(FingerprintInterceptor(context.applicationContext as NetworkRouter, userSession))
    }

    companion object {


        fun parse(data: String): RequestBody {
            val jsonObject = JSONObject(data).toString()
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    jsonObject)
        }

        fun getUserAgent(): String {
            return String.format(USER_AGENT_FORMAT, GlobalConfig.VERSION_NAME, "Android " + Build.VERSION.RELEASE);
        }
    }
}
