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
class TimberReportingTree(private val tags: List<String>) : Timber.DebugTree() {

    var userId: String = ""
    var partDeviceId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0
    var tagMaps: HashMap<String, Tag> = hashMapOf()

    init {
        populateTagMaps(tags)
    }

    fun setClientLogs(clientLogs: List<String>?) {
        if (clientLogs == null || clientLogs.isEmpty()) {
            LogManager.logentriesEnabled = true
            LogManager.scalyrEnabled = false
            LogManager.isPrimaryLogentries = true
            LogManager.isPrimaryScalyr = false
        } else {
            // curently logentries are always enabled.
            LogManager.logentriesEnabled = clientLogs.contains(Constants.CLIENT_LOGENTRIES)
            LogManager.scalyrEnabled = clientLogs.contains(Constants.CLIENT_SCALYR)
            LogManager.isPrimaryLogentries = (clientLogs[0] == Constants.CLIENT_LOGENTRIES)
            LogManager.isPrimaryScalyr = (clientLogs[0] == Constants.CLIENT_SCALYR)
        }
    }

    fun setQueryLimits(queryLimit: List<Int>?) {
        if (queryLimit != null) {
            LogManager.queryLimits = queryLimit
        }
    }

    override fun log(logPriority: Int, tag: String?, message: String, t: Throwable?) {
        globalScopeLaunch({
            val timeStamp = System.currentTimeMillis()
            if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG || LogManager.instance == null) {
                return@globalScopeLaunch
            }
            if (!message.startsWith(PREFIX) || tags.isEmpty()) {
                return@globalScopeLaunch
            }
            val messageSplit = message.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }
            if (messageSplit.size < SIZE_MESSAGE) {
                return@globalScopeLaunch
            }
            val messageKey = messageSplit[0].plus(DELIMITER).plus(messageSplit[1])
            val messageToBeSend = message.substringAfter(messageKey.plus(DELIMITER))
            tagMaps[messageKey]?.let {
                val priority = it.postPriority
                val classLine = tag ?: ""
                val processedMessage = getMessage(messageSplit[1], timeStamp, classLine, replaceNewline(messageToBeSend))
                LogManager.log(processedMessage, timeStamp, priority, messageSplit.first())
            }
        })
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "[%s:%s:%s]",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
    }

    private fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US).format(Date(timeStamp))
    }

    private fun getMessage(tag: String, timeStamp: Long, classLine: String, message: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("tag=")
            .append(tag)
            .append(DELIMITER)
        stringBuilder.append("timestamp=")
            .append(timeStamp)
            .append(DELIMITER)
        stringBuilder.append("time=")
            .append("'${getReadableTimeStamp(timeStamp)}'")
            .append(DELIMITER)
        stringBuilder.append("did=")
            .append(partDeviceId)
            .append(DELIMITER)
        stringBuilder.append("uid=")
            .append(userId)
            .append(DELIMITER)
        stringBuilder.append("vernm=")
            .append(versionName)
            .append(DELIMITER)
        stringBuilder.append("vercd=")
            .append(versionCode)
            .append(DELIMITER)
        stringBuilder.append("os=")
            .append(Build.VERSION.RELEASE)
            .append(DELIMITER)
        stringBuilder.append("device=")
            .append("'${Build.MODEL}'")
            .append(DELIMITER)
        stringBuilder.append("cls=")
            .append("'${classLine}'")
            .append(DELIMITER)
        stringBuilder.append("msg=")
            .append(message)
        return stringBuilder.toString()
    }

    private fun getPriority(tagPriority: String): Int {
        if (tagPriority == TAG_OFFLINE) {
            return PRIORITY_OFFLINE
        }
        return PRIORITY_ONLINE
    }

    private fun replaceNewline(message: String): String {
        return message.trim().replace("\\s+".toRegex(), " ")
    }

    private fun populateTagMaps(tags: List<String>?) {
        if (tags == null) {
            return
        }
        for (tag in tags) {
            val tagSplit = tag.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }
            if (tagSplit.size != SIZE_REMOTE_CONFIG_TAG) {
                continue
            }
            tagSplit[2].toDoubleOrNull()?.let {
                val randomNumber = Random().nextDouble() * MAX_RANDOM_NUMBER
                if (randomNumber <= it) {
                    val tagKey = StringBuilder()
                        .append(tagSplit[0])
                        .append(DELIMITER)
                        .append(tagSplit[1])
                        .toString()
                    tagMaps[tagKey] = Tag(getPriority(tagSplit[3]))
                }
            }
        }
    }

    companion object {
        const val DELIMITER = "#"
        const val MAX_RANDOM_NUMBER = 100

        const val SIZE_MESSAGE = 3
        const val SIZE_REMOTE_CONFIG_TAG = 4

        const val PREFIX = "P"
        const val P1 = "P1"
        const val P2 = "P2"

        const val TAG_OFFLINE = "offline"
        const val TAG_ONLINE = "online"

        const val PRIORITY_ONLINE = 2
        const val PRIORITY_OFFLINE = 1
    }

}
