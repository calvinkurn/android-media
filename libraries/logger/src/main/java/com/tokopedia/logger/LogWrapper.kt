package com.tokopedia.logger

import android.app.Application
import android.os.Build
import android.util.Log
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession
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
 * LogWrapper.log(priority, message); or
 * LogWrapper.log(priority, message, throwable);
 */
class LogWrapper(val application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    /**
     * To give "INFO" message log to logging server
     * INFO means generally useful information to log
     */
    private fun info(message: String) {
        launch {
            logger?.info(message)
        }
    }

    /**
     * To give "WARNING" message log to logging server
     * WARNING means Anything that can potentially cause application oddities,
     * but app can still handle the main functionality
     */
    private fun warning(message: String) {
        launch {
            logger?.warning(message)
        }
    }

    /**
     * To give "SEVERE" message log to logging server
     * SEVERE means something quite fatal was happened and the main function might not be working properly
     */
    private fun severe(message: String) {
        launch {
            logger?.severe(message)
        }
    }

    private fun logMessage(level: Level, message: String) {
        if (message.isEmpty()) {
            return
        }
        if (level == Level.SEVERE) {
            severe(message)
        } else if (level == Level.WARNING) {
            warning(message)
        }
    }

    private fun logThrowable(level: Level, message: String, throwable: Throwable) {
        launch {
            logger?.log(LogRecord(level, message).apply {
                this.thrown = throwable
            })
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
        var instance: LogWrapper? = null
        var logger: Logger? = null
            get() {
                if (field == null) {
                    initLogger()
                }
                return field
            }

        fun initLogger(){
            instance?.let {
                val logManager = LogManager.getLogManager()
                logManager.readConfiguration(it.application.resources.openRawResource(R.raw.logging))
                logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME)
            }
        }

        @JvmStatic
        fun init(application: Application) {
            instance = LogWrapper(application)
        }

        /**
         * To give message log to logging server
         * priority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(priority: Int, message: String) {
            if (toLevel(priority) == Level.OFF) {
                return
            }
            instance?.run {
                val messageWithUser = buildUserMessage() + "\n" + message
                if (priority == Log.ERROR) {
                    severe(messageWithUser)
                } else if (priority == Log.WARN) {
                    warning(messageWithUser)
                }
            }

        }

        /**
         * To give message log to logging server alongside with throwable if any
         * priority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(priority: Int, message: String?, throwable: Throwable?) {
            val level = toLevel(priority)
            if (level == Level.OFF) {
                return
            }
            instance?.run {
                val messageWithUser = buildUserMessage() + "\n" + (message ?: "")
                if (throwable != null) {
                    logThrowable(level, messageWithUser, throwable)
                } else {
                    logMessage(level, messageWithUser)
                }
            }
        }

        private fun toLevel(priority: Int): Level {
            return when (priority) {
                Log.ERROR -> Level.SEVERE
                Log.WARN -> Level.WARNING
                else -> Level.OFF
            }
        }
    }

}