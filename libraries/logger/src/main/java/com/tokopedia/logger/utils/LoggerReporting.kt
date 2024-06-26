package com.tokopedia.logger.utils

import android.os.Build
import com.google.gson.Gson
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.model.newrelic.NewRelicConfig
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

    /*
    * since server logger v3 to make it more flexible and support two endpoints use the API & SDK in New relic
    * So, we add the keys & tables field maps, to handle the logic in the repository
     */
    var tagMapsNrKey: HashMap<String, NewRelicConfig> = hashMapOf()
    var tagMapsNrTable: HashMap<String, String> = hashMapOf()

    fun getProcessedMessage(
        priority: Priority,
        tag: String,
        oriMessageMap: Map<String, String>,
        userId: String,
        activityName: String
    ): Logger? {
        val timeStamp = System.currentTimeMillis()
        val priorityText = when (priority) {
            Priority.P1 -> P1
            Priority.P2 -> P2
            else -> ""
        }
        val tagMapKey =
            StringBuilder(priorityText).append(DELIMITER_TAG_MAPS).append(tag).toString()

        var priorityTag = -1
        tagMapsScalyr[tagMapKey]?.let {
            priorityTag = it.postPriority
        }

        tagMapsNewRelic[tagMapKey]?.let {
            priorityTag = it.postPriority
        }


        if (priorityTag != -1) {
            var processedMessage = getMessage(tag, timeStamp, priorityText, oriMessageMap, userId, activityName)
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

    private fun getMessage(
        tag: String,
        timeStamp: Long,
        priority: String,
        message: Map<String, String>,
        userId: String,
        activityName: String
    ): String {
        val mapMessage = mutableMapOf<String, String>()
        val p = when (priority) {
            P1 -> Constants.SEVERITY_HIGH
            P2 -> Constants.SEVERITY_MEDIUM
            else -> -1
        }

        with(mapMessage) {
            // temporary for testing
            put("log_vernm", versionName)

            put(Constants.TAG_LOG, tag)
            put(Constants.PRIORITY_LOG, p.toString())

            if (tag == GP) {
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
                put("log_vercd", versionCode.toString())
                put("log_os", Build.VERSION.RELEASE)
                put("log_device", Build.MODEL)
                put("log_packageName", packageName.toString())
                put("log_installer", installer.toString())
                put("log_debug", debug.toString())
                put("activityName", activityName)
                for (item in message) {
                    val filterKey = getFilterKey(item.key)
                    if (item.value.length > Constants.MAX_LENGTH_PER_ITEM) {
                        put(filterKey, item.value.substring(0, Constants.MAX_LENGTH_PER_ITEM))
                    } else {
                        put(filterKey, item.value)
                    }
                }
            }
        }

        return mapMessage.convertMapToJsonString()
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
                    val (newRelicKey, newRelicTable) = getNewRelicKeyAndTable(tagSplit)
                    tagMapsNewRelic[tagKey] =
                        NewRelicTag(getPriority(tagSplit[3]), newRelicKey, newRelicTable)
                }
            }
        }
    }

    fun setPopulateKeyMapsNewRelic(nrKeys: List<String>?) {
        tagMapsNrKey.clear()

        if (nrKeys.isNullOrEmpty()) {
            return
        }
        for (key in nrKeys) {
            val nrKeySplit = key.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }

            if (nrKeySplit.size < SIZE_NR_KEY_SPLIT || nrKeySplit.isEmpty()) {
                continue
            }

            val nrKeyName = nrKeySplit[0]
            val nrUserId = nrKeySplit[1]
            val nrKeyValue = nrKeySplit[2]
            tagMapsNrKey[nrKeyName] = NewRelicConfig(nrUserId, nrKeyValue)
        }
    }

    fun setPopulateTableMapsNewRelic(nrTables: List<String>?) {
        tagMapsNrTable.clear()

        if (nrTables.isNullOrEmpty()) {
            return
        }
        for (table in nrTables) {
            val nrTableSplit =
                table.split(DELIMITER_TAG_MAPS.toRegex()).dropLastWhile { it.isEmpty() }

            if (nrTableSplit.size < SIZE_NR_TABLE_SPLIT || nrTableSplit.isEmpty()) {
                continue
            }

            val nrTableName = nrTableSplit[0]
            val nrTableValue = nrTableSplit[1]
            tagMapsNrTable[nrTableName] = nrTableValue
        }
    }

    private fun getFilterKey(key: String): String {
        return if (key == TYPE) {
            key.replace(TYPE, TYP)
        } else {
            key
        }
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

    private fun getNewRelicKeyAndTable(tagSplit: List<String>): Pair<String, String> {
        val nrKey = tagMapsNrKey.keys.firstOrNull { it in tagSplit } ?: ""
        val nrTable = tagMapsNrTable.keys.firstOrNull { it in tagSplit } ?: ""

        return Pair(nrKey, nrTable)
    }

    companion object {
        const val DELIMITER_TAG_MAPS = "#"
        const val DELIMITER = ","
        const val MAX_RANDOM_NUMBER = 100

        const val SIZE_NR_TABLE_SPLIT = 2
        const val SIZE_NR_KEY_SPLIT = 3

        const val SIZE_MESSAGE = 3
        const val SIZE_REMOTE_CONFIG_TAG = 4
        const val SIZE_REMOTE_CONFIG_NR_TAG = 6

        const val PREFIX = "P"
        const val P1 = "P1"
        const val P2 = "P2"
        const val SF = "SF"

        const val GP = "GP"

        const val TYPE = "type"
        const val TYP = "typ"

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
