package com.tokopedia.test.application.environment.interceptor.size

import com.tokopedia.analytics.performance.util.NetworkData
import com.tokopedia.network.BuildConfig
import com.tokopedia.test.application.util.parserule.ParserRuleProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.ConcurrentModificationException

class GqlNetworkAnalyzerInterceptor : Interceptor {

    companion object {
        val parserRuleProvider = ParserRuleProvider()
        val sizeInEachRequest = hashMapOf<String, Int>()
        val timeInEachRequest = hashMapOf<String, Long>()
        val queryCounterMap = hashMapOf<String, Int>()
        val gqlQueryListToAnalyze = mutableListOf<String>()
        val interceptedNetworkDataList = mutableListOf<InterceptedNetworkData>()

        var startRequest = 0L
        var endRequest = 0L

        @JvmStatic
        fun addGqlQueryListToAnalyze(gqlQueryListToAnalyze: List<String>?) {
            if (gqlQueryListToAnalyze == null) return

            this.gqlQueryListToAnalyze.addAll(gqlQueryListToAnalyze.map { it.toLowerCase(Locale.US) })
        }

        @JvmStatic
        fun reset() {
            sizeInEachRequest.clear()
            timeInEachRequest.clear()
            queryCounterMap.clear()
            gqlQueryListToAnalyze.clear()
            interceptedNetworkDataList.clear()
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

        fun getNetworkData(): NetworkData {
            try {
                processInterceptedNetworkData()
            } catch (e: ConcurrentModificationException) {
                e.printStackTrace()
            }

            return NetworkData(
                    getTotalSize(),
                    getTotalTime(),
                    getUserTotalNetworkDuration(),
                    sizeInEachRequest,
                    timeInEachRequest
            )
        }

        private fun processInterceptedNetworkData() {
            interceptedNetworkDataList.forEach { interceptedNetworkData ->
                val requestString = interceptedNetworkData.requestString
                val size = interceptedNetworkData.size
                val reqTimeStamp = interceptedNetworkData.requestTimeStamp
                val respTimeStamp = interceptedNetworkData.responseTimeStamp
                val duration = interceptedNetworkData.duration

                var formattedOperationName = parserRuleProvider.parse(requestString.substringAfter("\"query\": \""))

                var needToAnalyze = true
                if (gqlQueryListToAnalyze.isNotEmpty()) {
                    needToAnalyze = gqlQueryListToAnalyze.contains(formattedOperationName.toLowerCase(Locale.US))
                }

                if (needToAnalyze) {
                    if (queryCounterMap.containsKey(formattedOperationName)) {
                        queryCounterMap[formattedOperationName] = (queryCounterMap[formattedOperationName]
                                ?: 0) + 1
                        formattedOperationName += queryCounterMap[formattedOperationName]
                    } else {
                        queryCounterMap[formattedOperationName] = 1
                    }
                    sizeInEachRequest[formattedOperationName] = size
                    timeInEachRequest[formattedOperationName] = duration

                    if (startRequest == 0L) {
                        startRequest = reqTimeStamp
                    }
                    endRequest = respTimeStamp
                }
            }
        }
    }

    data class InterceptedNetworkData(
            val requestString: String,
            val size: Int,
            val requestTimeStamp: Long,
            val responseTimeStamp: Long
    ) {
        val duration = responseTimeStamp - requestTimeStamp
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                return analyzeNetwork(chain)
            } catch (e: IOException) {
                Timber.i("SizeInterceptorTag; %s", "did not work ${e.stackTrace}")
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError("SizeInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
        return chain.proceed(chain.request())
    }

    private fun analyzeNetwork(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestString = request.getRequestString()
        val response = chain.proceed(chain.request())
        val size = response.peekBody(Long.MAX_VALUE).bytes().size

        interceptedNetworkDataList.add(
                InterceptedNetworkData(
                        requestString = requestString,
                        size = size,
                        requestTimeStamp = response.sentRequestAtMillis(),
                        responseTimeStamp = response.receivedResponseAtMillis()
                )
        )

        return response
    }

    private fun Request.getRequestString(): String {
        val buffer = Buffer()
        this.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }
}