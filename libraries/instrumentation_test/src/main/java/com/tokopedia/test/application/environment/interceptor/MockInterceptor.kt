package com.tokopedia.test.application.environment.interceptor

import com.tokopedia.network.BuildConfig
import okhttp3.*
import okio.Buffer
import java.io.IOException

class MockInterceptor(val responseList: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val copy = chain.request().newBuilder().build()
                val buffer = Buffer()
                copy.body()?.writeTo(buffer)
                val requestString = buffer.readUtf8()

                var responseString = ""
                responseList.forEach {
                    if (requestString.contains(it.key)) {
                        responseString = it.value
                        return chain.proceed(chain.request())
                                .newBuilder()
                                .code(200)
                                .protocol(Protocol.HTTP_2)
                                .message(responseString)
                                .body(ResponseBody.create(MediaType.parse("application/json"),
                                        responseString.toByteArray()))
                                .addHeader("content-type", "application/json")
                                .build()
                    }
                }
            } catch (e: IOException) {
                "did not work"
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
        return chain.proceed(chain.request())
    }
}