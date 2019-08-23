package com.tokopedia.logger

import android.app.Application
import android.os.Build
import android.util.Log
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession
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

    private fun sendLogToServer(serverSeverity: Int, logPriority: Int, message: String) {
        launch {
            val messageWithUser =
                logToString(logPriority) + " " +
                    buildUserMessage() + " - " +
                    message
            val truncatedMessage: String
            if (message.length > MAX_BUFFER) {
                truncatedMessage = messageWithUser.substring(0, MAX_BUFFER).replace("\n",  " - ")
            } else {
                truncatedMessage = messageWithUser.replace("\n",  " - ")
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

    private fun logToString(logPriority: Int): String {
        return when (logPriority) {
            Log.ERROR -> "SEVR"
            Log.WARN -> "WARN"
            else -> "INFO"
        }
    }

    private fun buildUserMessage(): String {
        val userSession = UserSession(application)
        val userId = if (userSession.userId.isNullOrEmpty()) {
            ""
        } else {
            userSession.userId
        }
        return "#" + if (!userId.isEmpty()) {
            "uid=${userId}#"
        } else {
            ""
        } +
            "app=${GlobalConfig.getPackageApplicationName()}#" +
            "vernm=${GlobalConfig.VERSION_NAME}#" +
            "vercd=${GlobalConfig.VERSION_CODE}#" +
            "os=${Build.VERSION.RELEASE}#" +
            "device=${Build.MODEL}#"
    }

    companion object {
        const val MAX_BUFFER = 3900
        const val URL_LOGENTRIES = "https://us.webhook.logs.insight.rapid7.com/v1/noformat/"
        var instance: LogWrapper? = null
        val TOKEN: Array<String> = arrayOf(
            "08fcd148-14aa-4d89-ac67-4f70fefd2f37",
            "60664ea7-4d61-4df1-b39c-365dc647aced",
            "33acc8e7-1b5c-403e-bd31-7c1e61bbef2c")

        @JvmStatic
        fun init(application: Application) {
            instance = LogWrapper(application)
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(serverSeverity: Int, logPriority: Int, message: String) {
            instance?.run {
                sendLogToServer(serverSeverity, logPriority, message)
            }
        }

    }

}