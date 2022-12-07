package com.tokopedia.logger.utils

import android.os.Build
import com.google.gson.Gson
import com.tokopedia.logger.datasource.db.Logger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Class to process the message that will be sent to scalyr/new relic
 * Input: MessageMap, PX, Tag
 */
class LoggerReporting {

    var partDeviceId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0
    var installer: String? = ""
    var debug: Boolean? = false
    var packageName: String? = null
    var tagMapsScalyr: HashMap<String, Tag> = hashMapOf()
    var tagMapsNewRelic: HashMap<String, NewRelicTag> = hashMapOf()
    var tagMapsEmbrace: HashMap<String, Tag> = hashMapOf()

    /*
    * since server logger v3 to make it more flexible and support two endpoints use the API & SDK in New relic
    * So, we add the keys & tables field maps, to handle the logic in the repository
     */
    var tagMapsNrKey: HashMap<String, String> = hashMapOf()
    var tagMapsNrTable: HashMap<String, String> = hashMapOf()

    fun getProcessedMessage(
        priority: Priority,
        tag: String,
        oriMessageMap: Map<String, String>,
        userId: String
    ): Logger? {
        val timeStamp = System.currentTimeMillis()
        val priorityText = when (priority) {
            Priority.P1 -> P1
            Priority.P2 -> P2
            Priority.SF -> SF
        }
        val tagMapKey = StringBuilder(priorityText).append(DELIMITER_TAG_MAPS).append(tag).toString()

        var priorityTag = -1
        tagMapsScalyr[tagMapKey]?.let {
            priorityTag = it.postPriority
        }

        tagMapsNewRelic[tagMapKey]?.let {
            priorityTag = it.postPriority
        }

        tagMapsEmbrace[tagMapKey]?.let {
            priorityTag = it.postPriority
        }

        if (priorityTag != -1) {
            var processedMessage = getMessage(tag, timeStamp, priorityText, oriMessageMap, userId)
            processedMessage = if (processedMessage.length > Constants.MAX_BUFFER) {
                processedMessage.substring(0, Constants.MAX_BUFFER)
            } else {
                processedMessage
            }
            return Logger(timeStamp, priorityText, priorityTag, processedMessage)
        } else {
            return null
        }
    }

    private fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US).format(Date(timeStamp))
    }

    private fun getMessage(tag: String, timeStamp: Long, priority: String, message: Map<String, String>, userId: String): String {
        val mapMessage = mutableMapOf<String, String>()
        val p = when (priority) {
            P1 -> Constants.SEVERITY_HIGH
            P2 -> Constants.SEVERITY_MEDIUM
            SF -> Constants.SEVERITY_SF
            else -> -1
        }

        with(mapMessage) {
            put(Constants.PRIORITY_LOG, p.toString())
            put(Constants.TAG_LOG, tag)
            if (priority == SF) {
                for (item in message) {
                    if (item.value.length > Constants.MAX_LENGTH_PER_ITEM) {
                        put(item.key, item.value.substring(0, Constants.MAX_LENGTH_PER_ITEM))
                    } else {
                        put(item.key, item.value)
                    }
                }
            } else {
                put("log_timestamp", timeStamp.toString())
                put("log_time", getReadableTimeStamp(timeStamp))
                put("log_did", partDeviceId)
                put("log_uid", userId)
                put("log_vernm", versionName)
                put("log_vercd", versionCode.toString())
                put("log_os", Build.VERSION.RELEASE)
                put("log_device", Build.MODEL)
                put("log_packageName", packageName.toString())
                put("log_installer", installer.toString())
                put("log_debug", debug.toString())
                for (item in message) {
                    if (item.value.length > Constants.MAX_LENGTH_PER_ITEM) {
                        put(item.key, item.value.substring(0, Constants.MAX_LENGTH_PER_ITEM))
                    } else {
                        put(item.key, item.value)
                    }
                }
            }
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
        tagMapsScalyr.clear()
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
        tagMapsNewRelic.clear()
        if (tags.isNullOrEmpty()) {
            return
        }
        for (tag in tags) {
            val tagSplit = tag.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }
            if (tagSplit.size < SIZE_REMOTE_CONFIG_TAG || tagSplit.size > SIZE_REMOTE_CONFIG_NR_TAG) {
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
                    val newRelicKey = if (tagSplit.getOrNull(NR_KEY_INDEX) != null) tagSplit[NR_KEY_INDEX] else ""
                    val newRelicTable = if (tagSplit.getOrNull(NR_TABLE_INDEX) != null) tagSplit[NR_TABLE_INDEX] else Constants.EVENT_ANDROID_NEW_RELIC
                    tagMapsNewRelic[tagKey] = NewRelicTag(getPriority(tagSplit[3]), newRelicKey, newRelicTable)
                }
            }
        }
    }

    fun setPopulateKeyMapsNewRelic(nrKeys: List<String>?) {
        if (nrKeys.isNullOrEmpty()) {
            return
        }
        for (key in nrKeys) {
            val nrKeySplit = key.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }

            if (nrKeySplit.size < SIZE_NR_SPLIT || nrKeySplit.isEmpty()) {
                continue
            }

            val nrKeyName = nrKeySplit[0]
            val nrKeyValue = nrKeySplit[1]
            tagMapsNrKey[nrKeyName] = nrKeyValue
        }
    }

    fun setPopulateTableMapsNewRelic(nrTables: List<String>?) {
        if (nrTables.isNullOrEmpty()) {
            return
        }
        for (table in nrTables) {
            val nrTableSplit = table.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }

            if (nrTableSplit.size < SIZE_NR_SPLIT || nrTableSplit.isEmpty()) {
                continue
            }

            val nrTableName = nrTableSplit[0]
            val nrTableValue = nrTableSplit[1]
            tagMapsNrTable[nrTableName] = nrTableValue
        }
    }

    fun setPopulateTagMapsEmbrace(tags: List<String>?) {
        tagMapsEmbrace.clear()
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
                    tagMapsEmbrace[tagKey] = Tag(getPriority(tagSplit[3]))
                }
            }
        }
    }

    companion object {
        const val DELIMITER_TAG_MAPS = "#"
        const val DELIMITER = ","
        const val MAX_RANDOM_NUMBER = 100

        const val SIZE_NR_SPLIT = 2

        const val SIZE_MESSAGE = 3
        const val SIZE_REMOTE_CONFIG_TAG = 4
        const val SIZE_REMOTE_CONFIG_NR_TAG = 6

        const val PREFIX = "P"
        const val P1 = "P1"
        const val P2 = "P2"
        const val SF = "SF"

        const val NR_KEY_INDEX = 5
        const val NR_TABLE_INDEX = 6

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
