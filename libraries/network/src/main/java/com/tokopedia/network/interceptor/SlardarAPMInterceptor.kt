package com.tokopedia.network.interceptor

import androidx.annotation.Keep
import com.bytedance.apm.ApmAgent
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

@Keep
class SlardarAPMInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        var endTime: Long
        var duration: Long
        var sendTime: Long
        val url = request.url.toString()
        val remoteIp = request.url.host
        var status: Int
        var response: Response

        try {
            response = chain.proceed(request)
            endTime = System.currentTimeMillis()
            duration = endTime - startTime
            sendTime = request.header("X-Sent")?.toLongOrNull() ?: startTime
            status = response.code
            response
        } catch (e: IOException) {
            // Log the exception or perform any other action
            // Assign default values
            endTime = System.currentTimeMillis()
            duration = endTime - startTime
            sendTime = startTime
            status = 500 // Internal Server Error
            // Monitor SLA with default values
            ApmAgent.monitorApiError(duration, sendTime, url, remoteIp, "", status, JSONObject())
            ApmAgent.monitorSLA(duration, sendTime, url, remoteIp, "", status, JSONObject())
            // Re-throw the exception
            throw e
        }

        // Monitor SLA with captured values
        ApmAgent.monitorSLA(duration, sendTime, url, remoteIp, "", status, JSONObject())
        return response
    }

}
