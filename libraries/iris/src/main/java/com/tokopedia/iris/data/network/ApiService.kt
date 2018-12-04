package com.tokopedia.iris.data.network

import android.content.Context
import com.tokopedia.iris.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by meta on 21/11/18.
 */
class ApiService(context: Context) {

    private val session: Session = IrisSession(context)

    private var retrofit: Retrofit = createRetrofit().build()
    var apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

    private fun createRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createClient())
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor {
                    val original = it.request()
                    val request: Request = original.newBuilder()
                            .header(HEADER_CONTENT_TYPE, HEADER_JSON)
                            .header(HEADER_USER_ID, session.getUserId())
                            .header(HEADER_DEVICE, HEADER_ANDROID)
                            .method(original.method(), original.body())
                            .build()

                    it.proceed(request)
                }
                .addInterceptor(HttpLoggingInterceptor())
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build()
    }
}
