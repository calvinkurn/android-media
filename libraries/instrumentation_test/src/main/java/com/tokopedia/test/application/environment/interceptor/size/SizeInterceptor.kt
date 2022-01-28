package com.tokopedia.test.application.environment.interceptor.size

import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_QUERY_NAME
import okhttp3.*
import okio.Buffer
import java.io.IOException

class SizeInterceptor(private val sizeModelConfig: SizeModelConfig, private val adder: (Int) -> Unit) : Interceptor {

    companion object {
        const val KEY = "SizeInterceptor"
    }
    val sizeInEachRequest = hashMapOf<String, Int>()

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val copy = chain.request().newBuilder().build()
                val buffer = Buffer()
                copy.body?.writeTo(buffer)
                val response = chain.proceed(chain.request())

                sizeModelConfig.getResponseList().forEach {
                    if (it.value.findType == FIND_BY_CONTAINS || it.value.findType == FIND_BY_QUERY_NAME) {
                        if (!sizeInEachRequest.containsKey(it.key)) {
                            val size = response.body?.bytes()?.size ?: 0
                            adder.invoke(size)
                            sizeInEachRequest[it.key] = size
                        }
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