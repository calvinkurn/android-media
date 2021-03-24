package com.tokopedia.logger.utils

import android.os.Build
import com.google.gson.Gson
import com.tokopedia.logger.LogManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
class TimberReportingTree {

    var userId: String = ""
    var partDeviceId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0
    var installerPackageName: String? = ""
    var tagMaps: HashMap<String, Tag> = hashMapOf()

    constructor(tags: List<String>) {
        populateTagMaps(tags)
    }

    constructor()

    fun setClientLogs(clientLogs: List<String>?) {
        // noop. only has 1 client now.
    }

    fun setQueryLimits(queryLimit: List<Int>?) {
        if (queryLimit != null) {
            LogManager.queryLimits = queryLimit
        }
    }

    fun log(logPriority: String, tag: String, message: Map<String, String>) {
        globalScopeLaunch({
            val timeStamp = System.currentTimeMillis()

            val tagMapKey = StringBuilder(logPriority).append(DELIMITER_TAG_MAPS).append(tag).toString()

            if (LogManager.instance == null) {
                return@globalScopeLaunch
            }

            tagMaps[tagMapKey]?.let {
                val priorityTag = it.postPriority
                val processedMessage = getMessage(tag, timeStamp, logPriority, message)
                LogManager.log(processedMessage, timeStamp, priorityTag, logPriority)
            }
        })
    }

    private fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US).format(Date(timeStamp))
    }

    private fun getMessage(tag: String, timeStamp: Long, priority: String, message: Map<String, String>): String {
        val mapMessage = mutableMapOf<String, String>()
        val tokenIndex = when (priority) {
            P1 -> Constants.SEVERITY_HIGH - 1
            P2 -> Constants.SEVERITY_MEDIUM - 1
            else -> -1
        }
        val scalyrConfig = LogManager.scalyrConfigList.getOrNull(tokenIndex)
        with(mapMessage) {
            put("tag", tag)
            put("timestamp", timeStamp.toString())
            put("time", getReadableTimeStamp(timeStamp))
            put("did", partDeviceId)
            put("uid", userId)
            put("vernm", versionName)
            put("vercd", versionCode.toString())
            put("os", Build.VERSION.RELEASE)
            put("device", Build.MODEL)
            put("packageName", scalyrConfig?.packageName.orEmpty())
            put("installer", scalyrConfig?.installer.orEmpty())
            put("debug", scalyrConfig?.debug.toString())
            put("priority", scalyrConfig?.priority.toString())
            putAll(message)
        }

        return mapMessage.convertMapToJsonString()
    }

    private fun getPriority(tagPriority: String): Int {
        if (tagPriority == TAG_OFFLINE) {
            return PRIORITY_OFFLINE
        }
        return PRIORITY_ONLINE
    }

    private fun Map<String, String>.convertMapToJsonString(): String {
        return Gson().toJson(this)
    }

    private fun populateTagMaps(tags: List<String>?) {
        if (tags == null) {
            return
        }
        for (tag in tags) {
            val tagSplit = tag.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }
            if (tagSplit.size != SIZE_REMOTE_CONFIG_TAG) {
                continue
            }
            tagSplit[2].toDoubleOrNull()?.let {
                val randomNumber = Random().nextDouble() * MAX_RANDOM_NUMBER
                if (randomNumber <= it) {
                    val tagKey = StringBuilder()
                            .append(tagSplit[0])
                            .append(DELIMITER_TAG_MAPS)
                            .append(tagSplit[1])
                            .toString()
                    tagMaps[tagKey] = Tag(getPriority(tagSplit[3]))
                }
            }
        }
    }

    companion object {
        const val DELIMITER_TAG_MAPS = "#"
        const val DELIMITER = ","
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
