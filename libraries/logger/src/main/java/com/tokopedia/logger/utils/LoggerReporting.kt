package com.tokopedia.logger.utils

import android.os.Build
import com.google.gson.Gson
import com.tokopedia.logger.LogManager
import java.text.SimpleDateFormat
import java.util.*

class LoggerReporting {

    var partDeviceId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0
    var installer: String? = ""
    var debug: Boolean? = false
    var packageName: String? = null
    var tagMapsScalyr: HashMap<String, Tag> = hashMapOf()
    var tagMapsNewRelic: HashMap<String, Tag> = hashMapOf()

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

            var priorityTag = -1
            tagMapsScalyr[tagMapKey]?.let {
                priorityTag = it.postPriority
            }

            tagMapsNewRelic[tagMapKey]?.let {
                priorityTag = it.postPriority
            }

            if (priorityTag != -1) {
                val userId = LogManager.instance?.loggerProxy?.userId ?: ""
                val processedMessage = getMessage(tag, timeStamp, logPriority, message, userId)
                LogManager.log(processedMessage, timeStamp, priorityTag, logPriority)
            }
        })
    }

    private fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US).format(Date(timeStamp))
    }

    private fun getMessage(tag: String, timeStamp: Long, priority: String, message: Map<String, String>, userId:String): String {
        val mapMessage = mutableMapOf<String, String>()
        val p = when (priority) {
            P1 -> Constants.SEVERITY_HIGH
            P2 -> Constants.SEVERITY_MEDIUM
            else -> -1
        }
        with(mapMessage) {
            put("log_tag", tag)
            put("log_timestamp", timeStamp.toString())
            put("log_time", getReadableTimeStamp(timeStamp))
            put("log_did", partDeviceId)
            put("log_uid",  userId)
            put("log_vernm", versionName)
            put("log_vercd", versionCode.toString())
            put("log_os", Build.VERSION.RELEASE)
            put("log_device", Build.MODEL)
            put("log_packageName", packageName.toString())
            put("log_installer", installer.toString())
            put("log_debug", debug.toString())
            put("log_priority",p.toString())
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

    fun setPopulateTagMapsScalyr(tags: List<String>?) {
        if (tags.isNullOrEmpty()) {
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
                    tagMapsScalyr[tagKey] = Tag(getPriority(tagSplit[3]))
                }
            }
        }
    }

    fun setPopulateTagMapsNewRelic(tags: List<String>?) {
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
                    tagMapsNewRelic[tagKey] = Tag(getPriority(tagSplit[3]))
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

        @Volatile
        private var INSTANCE: LoggerReporting? = null

        fun getInstance(): LoggerReporting {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoggerReporting().also { INSTANCE = it }
            }
        }
    }

}
