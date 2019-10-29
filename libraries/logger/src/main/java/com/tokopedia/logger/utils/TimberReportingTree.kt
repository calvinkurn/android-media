package com.tokopedia.logger.utils

import android.util.Log

import com.tokopedia.logger.LogManager

import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.LinkedList

import timber.log.Timber

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
class TimberReportingTree(private val tags: List<String>?, private val appVersionCode: Long?) : Timber.DebugTree() {

    private var serverSeverity: Int = 0

    private val timeStamp: Long
        get() = System.currentTimeMillis()

    override fun log(logPriority: Int, tag: String?, message: String, t: Throwable?) {
        var message = message
        // Get time stamp the moment log is called
        val timeStamp = timeStamp
        val priority = getPriority(message)

        if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG || LogManager.instance == null) {
            return
        }
        // only log the message starts with P

        // Checking based on config
        if (message.startsWith(PREFIX) && tags != null) {
            for (tagString in tags) {
                val tagSplit = LinkedList(Arrays.asList(*tagString.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
                if (tagSplit.size == 2) {
                    tagSplit.add(2, "online")
                }
                if (message.contains(tagSplit[0])) {
                    if (message.contains(tagSplit[1])) {
                        serverSeverity = setSeverity(message)
                        message = getMessage(message)
                        LogManager.log(logToString(logPriority) + "#" + message + "#" + tag, timeStamp, priority)
                    } else if (!message.contains("offline")) {
                        serverSeverity = setSeverity(message)
                        message = getMessage(message)
                        LogManager.log(logToString(logPriority) + "#" + message + "#" + tag, timeStamp, priority)
                    }// Catches if user forgets to enter the "online" or "offline" in the message
                }
            }
        }
    }

    private fun logToString(logPriority: Int): String {
        when (logPriority) {
            Log.ERROR -> return "E"
            Log.WARN -> return "W"
            else -> return "I"
        }
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format("[%s:%s:%s]",
                super.createStackElementTag(element),
                element.methodName,
                element.lineNumber)
    }

    private fun getReadableTimeStamp(timeStamp: Long?): String {
        return SimpleDateFormat(Constants.TIMESTAMP_FORMAT).format(Date(timeStamp!!))
    }

    private fun getAppVersionName(appVersionCode: Long): String {
        val appVersionCodeStr = appVersionCode.toString()
        return appVersionCodeStr.substring(3, 5) +
                "." + appVersionCodeStr.substring(5, 7) +
                "." + appVersionCodeStr.substring(7)
    }

    private fun getMessage(message: String): String {
        return getReadableTimeStamp(timeStamp) + "#" + appVersionCode + "#" + getAppVersionName(appVersionCode!!) + "#UserID=1#" + message
    }

    private fun setSeverity(message: String): Int {
        if (message.startsWith(P1)) {
            serverSeverity = SEVERITY_HIGH
        } else if (message.startsWith(P2)) {
            serverSeverity = SEVERITY_MEDIUM
        } else {
            serverSeverity = NO_SEVERITY
        }
        return serverSeverity
    }

    private fun getPriority(message: String): Int {
        val messageSplit = message.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var priority = 0
        if (messageSplit[2] == "offline") {
            priority = 1
            return priority
        } else {
            return priority
        }
    }

    companion object {

        val PREFIX = "P"
        val P1 = "P1"
        val P2 = "P2"

        val SEVERITY_HIGH = 1
        val SEVERITY_MEDIUM = 2
        val NO_SEVERITY = 0
    }

}