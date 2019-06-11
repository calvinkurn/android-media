package com.tokopedia.logger

import android.app.Application
import android.util.Log
import com.tokopedia.user.session.UserSession
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import io.sentry.event.UserBuilder
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

/**
 * Class to wrap the mechanism to send the logging message to server.
 * For the current implementation, this class is wrapping the Sentry library
 *
 * To Initialize:
 * LogSentryWrapper.init(application);
 *
 * To send message to server:
 * LogSentryWrapper.log(priority, message); or
 * LogSentryWrapper.log(priority, message, throwable);
 */
class LogSentryWrapper(val application: Application) {

    val logger: Logger by lazy {
        val logManager = LogManager.getLogManager()
        logManager.readConfiguration(application.resources.openRawResource(R.raw.logging_sentries))
        logManager.getLogger(Logger.GLOBAL_LOGGER_NAME)
    }

    val userSession: UserSession by lazy {
        UserSession(application)
    }

    /**
     * To give "INFO" message log to logging server
     * INFO means generally useful information to log
     */
    private fun info(message: String) {
        setUser()
        logger.info(message)
    }

    /**
     * To give "WARNING" message log to logging server
     * WARNING means Anything that can potentially cause application oddities,
     * but app can still handle the main functionality
     */
    private fun warning(message: String) {
        setUser()
        logger.warning(message)
    }

    /**
     * To give "SEVERE" message log to logging server
     * SEVERE means something quite fatal was happened and the main function might not be working properly
     */
    private fun severe(message: String) {
        setUser()
        logger.severe(message)
    }

    private fun severe(throwable: Throwable) {
        setUser()
        logger.log(Level.SEVERE, "", throwable)
    }

    private fun setUser() {
        if (userSession.userId.isNullOrEmpty()) {
            Sentry.getContext().user = UserBuilder().setId("").build()
        } else {
            Sentry.getContext().user = UserBuilder().setId(userSession.userId).build()
        }
    }

    private fun logMessage(priority: Int, message: String) {
        if (message.isEmpty()) {
            return
        }
        if (priority == Log.ERROR) {
            severe(message)
        } else if (priority == Log.WARN) {
            warning(message)
        }
    }

    private fun logThrowable(priority: Int, message: String?, throwable: Throwable?) {
        if (throwable != null && message.isNullOrEmpty()) {
            severe(throwable)
        } else {
            logMessage(priority, message ?: "")
        }
    }

    companion object {
        var instance: LogSentryWrapper? = null

        @JvmStatic
        fun init(application: Application) {
            instance = LogSentryWrapper(application)
            Sentry.init(AndroidSentryClientFactory(application));
        }

        /**
         * To give message log to logging server
         * priority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(priority: Int, message: String) {
            if (!isLoggable(priority)) {
                return
            }
            instance?.run {
                logMessage(priority, message)
            }

        }

        /**
         * To give message log to logging server alongside with throwable if any
         * priority to be handled are: Log.ERROR, Log.WARNING
         */
        @JvmStatic
        fun log(priority: Int, message: String?, throwable: Throwable?) {
            if (!isLoggable(priority)) {
                return
            }
            instance?.run {
                logThrowable(priority,message,throwable)
            }
        }

        private fun isLoggable(priority: Int): Boolean {
            return when (priority) {
                Log.ERROR,
                Log.WARN -> true
                else -> false
            }
        }
    }

}