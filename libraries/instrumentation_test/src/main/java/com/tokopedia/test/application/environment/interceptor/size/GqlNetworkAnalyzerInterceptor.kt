package com.tokopedia.test.application.environment.interceptor.size

import android.util.Log
import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig.Companion.FIND_BY_QUERY_NAME
import com.tokopedia.test.application.util.parserule.ParserRuleProvider
import okhttp3.*
import okio.Buffer
import java.io.IOException

class GqlNetworkAnalyzerInterceptor : Interceptor {

    companion object {
        val parserRuleProvider = ParserRuleProvider()
        val sizeInEachRequest = hashMapOf<String, Int>()
        val timeInEachRequest = hashMapOf<String, Long>()
        val queryCounterMap = hashMapOf<String, Int>()

        var startRequest = 0L
        var endRequest = 0L

        fun reset() {
            sizeInEachRequest.clear()
            timeInEachRequest.clear()
            queryCounterMap.clear()
            startRequest = 0L
            endRequest = 0L
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

        fun getUserTotalNetworkDuration(): Long {
            return endRequest - startRequest
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
                var formattedOperationName = parserRuleProvider.parse(requestString.substringAfter("\"query\": \""))
                if (queryCounterMap.containsKey(formattedOperationName)) {
                    queryCounterMap[formattedOperationName] = (queryCounterMap[formattedOperationName]?: 0) + 1
                    formattedOperationName += queryCounterMap[formattedOperationName]
                } else {
                    queryCounterMap[formattedOperationName] = 1
                }
                sizeInEachRequest[formattedOperationName] = size
                val reqTimeStamp = response.sentRequestAtMillis()
                val respTimeStamp = response.receivedResponseAtMillis()
                val duration = respTimeStamp - reqTimeStamp
                timeInEachRequest[formattedOperationName] = duration

                if (startRequest == 0L) {
                    startRequest = reqTimeStamp
                }
                endRequest = respTimeStamp

                return response
            } catch (e: IOException) {
                Log.i("SizeInterceptorTag", "did not work" + e.stackTrace)
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError("SizeInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
        return chain.proceed(chain.request())
    }
}