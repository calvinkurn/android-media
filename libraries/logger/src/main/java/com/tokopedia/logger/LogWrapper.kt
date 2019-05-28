package com.tokopedia.logger

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

class LogWrapper(val application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    companion object {
        var instance: LogWrapper? = null
        val logger: Logger by lazy {
            val logManager = LogManager.getLogManager()
            logManager.readConfiguration(instance!!.application.resources.openRawResource(R.raw.logging))
            logManager.getLogger(Logger.GLOBAL_LOGGER_NAME)
        }

        @JvmStatic
        fun init(application: Application) {
            instance = LogWrapper(application)
        }

        /**
         * To give "INFO" message log to logging server
         * INFO means generally useful information to log
         */
        @JvmStatic
        fun info(message: String) {
            instance?.launch {
                logger.info(message)
            }
        }

        /**
         * To give "WARNING" message log to logging server
         * WARNING means Anything that can potentially cause application oddities,
         * but app can still handle the main functionality
         */
        @JvmStatic
        fun warning(message: String) {
            instance?.launch {
                logger.warning(message)
            }
        }

        /**
         * To give "SEVERE" message log to logging server
         * SEVERE means something quite fatal was happened and the main function might not be working properly
         */
        @JvmStatic
        fun severe(message: String) {
            instance?.launch {
                logger.severe(message)
            }
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
            if (priority == Log.ERROR) {
                severe(message)
            } else if (priority == Log.WARN) {
                warning(message)
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
            if (throwable != null) {
                logThrowable(level, message ?: "", throwable)
            } else {
                logMessage(level, message ?: "")
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
            instance?.launch {
                logger.log(LogRecord(level, message).apply {
                    this.thrown = throwable
                })
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