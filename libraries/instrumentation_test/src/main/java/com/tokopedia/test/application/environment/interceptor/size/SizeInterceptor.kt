package com.tokopedia.test.application.environment.interceptor.size

import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_QUERY_NAME
import okhttp3.*
import okio.Buffer
import java.io.IOException

class SizeInterceptor : Interceptor {

    companion object {
        val sizeInEachRequest = hashMapOf<String, Int>()
        val timeInEachRequest = hashMapOf<String, Long>()

        fun reset() {
            sizeInEachRequest.clear()
            timeInEachRequest.clear()
        }

        fun getTotalSize(): Int {
            var total = 0
            sizeInEachRequest.forEach {
                total += it.value
            }
            return total
        }

        fun getTotalTime(): Long {
            var total = 0L
            timeInEachRequest.forEach {
                total += it.value
            }
            return total
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val request = chain.request()
                val buffer = Buffer()
                request.body()?.writeTo(buffer)
                val requestString = buffer.readUtf8()
                val response = chain.proceed(chain.request())

                val size = response.peekBody(Long.MAX_VALUE).bytes().size
                sizeInEachRequest[requestString] = size
                val reqTimeStamp = response.sentRequestAtMillis();
                val respTimeStamp = response.receivedResponseAtMillis()
                val duration = respTimeStamp - reqTimeStamp
                timeInEachRequest[requestString] = duration
                return response
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