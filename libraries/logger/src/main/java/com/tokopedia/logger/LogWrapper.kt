package com.tokopedia.logger

import android.app.Application
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

/**
 * Class to wrap the mechanism to send the logging message to server.
 * For the current implementation, this class is wrapping the insight7 (Logentries)
 *
 * To Initialize:
 * LogWrapper.init(application);
 *
 * To send message to server:
 * LogWrapper.log(serverSeverity, priority, message)
 */
class LogWrapper(val application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + handler

    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex -> }
    }
    var TOKEN: Array<String> = arrayOf()

    private fun sendLogToServer(serverSeverity: Int, message: String) {
        launch {
            val truncatedMessage: String
            if (message.length > MAX_BUFFER) {
                truncatedMessage = message.substring(0, MAX_BUFFER)
            } else {
                truncatedMessage = message
            }
            val token = TOKEN[serverSeverity - 1]
            var urlConnection: HttpURLConnection? = null
            val url: URL

            try {
                url = URL(URL_LOGENTRIES + token)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                val wr = DataOutputStream(urlConnection.getOutputStream())
                wr.writeBytes(truncatedMessage)
                wr.flush()
                wr.close()

                urlConnection.responseCode

            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }
    }

    fun setLogentriesToken(tokenList: Array<String>) {
        TOKEN = tokenList
    }

    companion object {
        const val MAX_BUFFER = 3900
        const val URL_LOGENTRIES = "https://us.webhook.logs.insight.rapid7.com/v1/noformat/"

        @JvmField
        var instance: LogWrapper? = null

        @JvmStatic
        fun init(application: Application) {
            instance = LogWrapper(application)
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(serverSeverity: Int, message: String) {
            instance?.run {
                sendLogToServer(serverSeverity, message)
            }
        }

    }

}