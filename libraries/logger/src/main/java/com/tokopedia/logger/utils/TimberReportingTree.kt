package com.tokopedia.logger.utils

import android.os.Build
import android.util.Log
import com.tokopedia.logger.LogManager
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
class TimberReportingTree(private val tags: List<String>?) : Timber.DebugTree() {

    var userId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0

    override fun log(logPriority: Int, tag: String?, message: String, t: Throwable?) {
        if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG || LogManager.instance == null) {
            return
        }
        // Only log the message starts with P
        if (!message.startsWith(PREFIX) || tags == null || tags.isEmpty()) {
            return
        }
        val messageSplit = LinkedList(listOf(*message.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        if (messageSplit.size < MIN_ARRAY_SIZE_MESSAGE) {
            return
        }
        // Checking based on config
        for (tagString in tags) {
            val tagSplit = LinkedList(listOf(*tagString.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
            if (tagSplit[0] == messageSplit[0] &&
                    tagSplit[1] == messageSplit[1]) {
                // Get time stamp the moment log is called
                val timeStamp = System.currentTimeMillis()
                val priority = getPriority(tagString)
                val classLine = tag ?: ""
                LogManager.log(getMessage(messageSplit[1], timeStamp, classLine, messageSplit[2]), timeStamp, priority, tagSplit[0])
            }
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

    private fun getMessage(tag: String, timeStamp:Long, classLine: String, message:String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("tag=")
                .append(tag)
                .append("#")
        stringBuilder.append("timestamp=")
                .append(timeStamp)
                .append("#")
        stringBuilder.append("time=")
                .append(getReadableTimeStamp(timeStamp))
                .append("#")
        stringBuilder.append("uid=")
                .append(userId)
                .append("#")
        stringBuilder.append("vernm=")
                .append(versionName)
                .append("#")
        stringBuilder.append("vercd=")
                .append(versionCode)
                .append("#")
        stringBuilder.append("os=")
                .append(Build.VERSION.RELEASE)
                .append("#")
        stringBuilder.append("device=")
                .append(Build.MODEL)
                .append("#")
        stringBuilder.append("cls=")
                .append(classLine)
                .append("#")
        stringBuilder.append("msg=")
                .append(message)
        return stringBuilder.toString()
    }

    private fun getPriority(tag: String): Int {
        val tagSplit = tag.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (tagSplit.size < 3) {
            return PRIORITY_ONLINE
        }
        if (tagSplit[2] == TAG_OFFLINE) {
            return PRIORITY_OFFLINE
        }
        return PRIORITY_ONLINE
    }

    companion object {
        const val MIN_ARRAY_SIZE_MESSAGE = 3
        const val MIN_ARRAY_SIZE_REMOTE_CONFIG_TAG = 3

        const val PREFIX = "P"
        const val P1 = "P1"
        const val P2 = "P2"

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2
        const val NO_SEVERITY = 0

        const val TAG_OFFLINE = "offline"
        const val TAG_ONLINE = "online"

        const val PRIORITY_ONLINE = 0
        const val PRIORITY_OFFLINE = 1
    }

}