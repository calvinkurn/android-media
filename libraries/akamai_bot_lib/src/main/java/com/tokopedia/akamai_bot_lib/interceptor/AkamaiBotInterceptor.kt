package com.tokopedia.akamai_bot_lib.interceptor

import android.content.Context
import com.akamai.botman.CYFMonitor
import com.tokopedia.akamai_bot_lib.*
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


class AkamaiBotInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)

        var newRequest: Request.Builder = chain.request().newBuilder()

        val akamaiValue = setExpire(
                { System.currentTimeMillis() },
                { context.getExpiredTime() },
                { time -> context.setExpiredTime(time) },
                { context.setAkamaiValue(CYFMonitor.getSensorData()) },
                { context.getAkamaiValue() }
        )

        newRequest.addHeader("X-acf-sensor-data",
                if (akamaiValue.isNotEmpty()) akamaiValue else "")

        val response = chain.proceed(newRequest.build())

        if (response.code == ERROR_CODE && response.header(HEADER_AKAMAI_KEY)?.contains(HEADER_AKAMAI_VALUE, true) == true) {
            logError(response)
            throw AkamaiErrorException(ERROR_MESSAGE_AKAMAI)
        }
        return response
    }

    private fun logError(response: Response){
        var messageMap: Map<String, String>? = mapOf(
            "request_body" to response.request.toString(),
            "user-agent" to response.request.header("User-Agent").toString(),
            "response" to response.peekBody(1024).string()
        )
        if (messageMap != null) {
            ServerLogger.log(Priority.P1, "AKAMAI_403_ERROR", messageMap)
        }
    }

//This method can be used to obtain users IP address. But please before doing that refer to the Personal Data Protection Policy
    private fun getLocalIpAddress(): String {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ""
    }

}