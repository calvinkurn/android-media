package com.tokopedia.logger

import android.app.Application
import android.os.Build
import android.util.Log
import com.rapid7.jul.LogentriesHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

/**
 * Class to wrap the mechanism to send the logging message to server.
 * For the current implementation, this class is wrapping the insight7 (Logentries)
 *
 * To Initialize:
 * LogWrapper.init(application);
 *
 * To send message to server:
 * LogWrapper.log(serverSeverity, priority, message); or
 * LogWrapper.log(serverSeverity, priority, message, throwable);
 * Server category: 1 means priority no. 1 (server) (highest)
 * Server category: 2 means priority no. 2 (server)
 */
class LogWrapper(val application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + handler

    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex -> }
    }

    /**
     * To give "INFO" message log to logging server
     * INFO means generally useful information to log
     */
    private fun info(serverSeverity: Int, message: String) {
        getLogger(serverSeverity)?.let {
            launch {
                it.info(message)
            }
        }
    }

    /**
     * To give "WARNING" message log to logging server
     * WARNING means Anything that can potentially cause application oddities,
     * but app can still handle the main functionality
     */
    private fun warning(serverSeverity: Int, message: String) {
        getLogger(serverSeverity)?.let {
            launch {
                it.warning(message)
            }
        }
    }

    /**
     * To give "SEVERE" message log to logging server
     * SEVERE means something quite fatal was happened and the main function might not be working properly
     */
    private fun severe(serverSeverity: Int, message: String) {
        getLogger(serverSeverity)?.let {
            launch {
                it.severe(message)
            }
        }
    }

    private fun logMessage(serverSeverity: Int, level: Level, message: String) {
        if (message.isEmpty()) {
            return
        }
        if (level == Level.SEVERE) {
            severe(serverSeverity, message)
        } else if (level == Level.WARNING) {
            warning(serverSeverity, message)
        }
    }

    private fun logThrowable(serverSeverity: Int, level: Level, message: String, throwable: Throwable) {
        getLogger(serverSeverity)?.let {
            launch {
                it.log(LogRecord(level, message).apply {
                    this.thrown = throwable
                })
            }
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
        const val LOGGER_NAME = "logentries"
        var instance: LogWrapper? = null
        val TOKEN: Array<String> = arrayOf(
            "0b653435-bd44-4ada-af6d-a511b42b2b08",
            "b774630c-039b-4d58-aac2-6332fbc40712",
            "78f40b14-98c6-435b-ae95-3f33cb85e199")

        const val REGION = "us"
        const val PORT = 10000

        internal var loggers: ArrayList<Logger> = arrayListOf()

        fun getLogger(serverSeverity: Int): Logger? {
            return try {
                return loggers.get(serverSeverity - 1)
            } catch (e: Throwable) {
                null
            }
        }

        @JvmStatic
        fun init(application: Application) {
            instance = LogWrapper(application)
            loggers.clear()
            LogManager.getLogManager().readConfiguration(application.resources.openRawResource(R.raw.logging))
            instance?.launch {
                for (i in 0 until TOKEN.size) {
                    loggers.add(Logger.getLogger(LOGGER_NAME + i))
                    loggers.get(i).addHandler(LogentriesHandler().apply {
                        region = REGION
                        token = TOKEN[i].toByteArray()
                        port = PORT
                        formatter = LogFormatter()
                    })
                    loggers.get(i).useParentHandlers = false
                }
            }
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(serverSeverity: Int, logPriority: Int, message: String) {
            if (toLevel(logPriority) == Level.OFF) {
                return
            }
            instance?.run {
                val messageWithUser = buildUserMessage() + "\n" + message
                if (logPriority == Log.ERROR) {
                    severe(serverSeverity, messageWithUser)
                } else if (logPriority == Log.WARN) {
                    warning(serverSeverity, messageWithUser)
                }
            }

        }

        /**
         * To give message log to logging server alongside with throwable if any
         * priority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(serverSeverity: Int, priority: Int, message: String?, throwable: Throwable?) {
            val level = toLevel(priority)
            if (level == Level.OFF) {
                return
            }
            instance?.run {
                val messageWithUser = buildUserMessage() + "\n" + (message ?: "")
                if (throwable != null) {
                    logThrowable(serverSeverity, level, messageWithUser, throwable)
                } else {
                    logMessage(serverSeverity, level, messageWithUser)
                }
            }
        }

        internal fun toLevel(logPriority: Int): Level {
            return when (logPriority) {
                Log.ERROR -> Level.SEVERE
                Log.WARN -> Level.WARNING
                else -> Level.OFF
            }
        }
    }

}