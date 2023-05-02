package com.tokopedia.logger.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudEmbraceImpl
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicApiImpl
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicSdkImpl
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.LoggerCloudModelWrapper
import com.tokopedia.logger.model.embrace.EmbraceBody
import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.model.scalyr.ScalyrEvent
import com.tokopedia.logger.model.scalyr.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.LoggerReporting
import com.tokopedia.logger.utils.addValue
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class LoggerRepository(
    private val gson: Gson,
    private val logDao: LoggerDao,
    private val loggerCloudScalyrDataSource: LoggerCloudDataSource,
    private val loggerCloudNewRelicSdkImpl: LoggerCloudNewRelicSdkImpl,
    private val loggerCloudNewRelicApiImpl: LoggerCloudNewRelicApiImpl,
    private val loggerCloudEmbraceImpl: LoggerCloudEmbraceImpl,
    private val scalyrConfigs: List<ScalyrConfig>,
    private val encrypt: ((String) -> (String))? = null,
    val decrypt: ((String) -> (String))? = null,
    private val decryptNrKey: ((String) -> (String))? = null
) : LoggerRepositoryContract, CoroutineScope {

    override suspend fun insert(logger: Logger) {
        var encryptedLogger = logger
        if (encrypt != null) {
            encryptedLogger = logger.copy(message = encrypt.invoke(logger.message))
        }
        logDao.insert(encryptedLogger)
    }

    override suspend fun getCount(): Int {
        return logDao.getCountAll()
    }

    override suspend fun deleteEntry(timeStamp: Long) {
        logDao.deleteEntry(timeStamp)
    }

    override suspend fun deleteEntries(loggers: List<Logger>) {
        logDao.deleteEntries(loggers)
    }

    override suspend fun deleteLog(logger: Logger) {
        logDao.deleteLog(logger)
    }

    override suspend fun deleteExpiredData() {
        val currentTimestamp = System.currentTimeMillis()
        logDao.deleteExpiredHighPrio(currentTimestamp - Constants.OFFLINE_TAG_THRESHOLD)
        logDao.deleteExpiredLowPrio(currentTimestamp - Constants.ONLINE_TAG_THRESHOLD)
    }

    override suspend fun sendLogToServer(queryLimits: List<Int>) {
        sendLogToServer(
            Constants.SEVERITY_HIGH,
            logDao.getServerChannel(LoggerReporting.P1, queryLimits[0])
        )
        sendLogToServer(
            Constants.SEVERITY_MEDIUM,
            logDao.getServerChannel(LoggerReporting.P2, queryLimits[1])
        )
    }

    // start region for view server logger in developer options
    override suspend fun getLoggerList(
        serverChannel: String,
        limit: Int,
        offset: Int
    ): List<Logger> {
        return if (serverChannel.isBlank()) {
            logDao.getLoggerList(
                limit,
                offset
            )
        } else {
            logDao.getLoggerListFilter(serverChannel, limit, offset)
        }
    }

    override suspend fun deleteAll() {
        logDao.deleteAll()
    }
    // end region for view server logger in developer options

    private suspend fun sendLogToServer(priorityScalyr: Int, logs: List<Logger>) {
        val priorityScalyrIndex = priorityScalyr - 1

        coroutineScope {
            launch {
                val jobList = mutableListOf<Deferred<Boolean>>()

                val mappedList = mapLogs(logs)
                val scalyrMessageList = mappedList.scalyrMessageList
                val newRelicMessageSdkList = mappedList.newRelicMessageSdkList
                val newRelicMessageApiList = mappedList.newRelicMessageApiMap
                val embraceMessageList = mappedList.embraceMessageList

                if (scalyrMessageList.isNotEmpty()) {
                    val jobScalyr = async {
                        sendScalyrLogToServer(
                            scalyrConfigs[priorityScalyrIndex],
                            logs,
                            scalyrMessageList
                        )
                    }
                    jobList.add(jobScalyr)
                }

                if (newRelicMessageSdkList.isNotEmpty()) {
                    val jobNewRelicSdk =
                        async { sendNewRelicSdkLogToServer(logs, newRelicMessageSdkList) }
                    jobList.add(jobNewRelicSdk)
                }

                if (newRelicMessageApiList.isNotEmpty()) {
                    val jobNewRelicApi = async {
                        sendNewRelicApiLogToServer(newRelicMessageApiList, logs)
                    }
                    jobList.add(jobNewRelicApi)
                }

                if (embraceMessageList.isNotEmpty()) {
                    val jobEmbrace = async { sendEmbraceLogToServer(logs, embraceMessageList) }
                    jobList.add(jobEmbrace)
                }

                val isSuccess = jobList.toList().awaitAll().any { it }
                if (isSuccess) {
                    deleteEntries(logs)
                }
            }
        }
    }

    private suspend fun sendScalyrLogToServer(
        config: ScalyrConfig,
        logs: List<Logger>,
        scalyrEventList: List<ScalyrEvent>
    ): Boolean {
        if (logs.isEmpty()) {
            return true
        }

        return loggerCloudScalyrDataSource.sendLogToServer(config, scalyrEventList)
    }

    private fun mapLogs(logs: List<Logger>): LoggerCloudModelWrapper {
        val scalyrEventList = mutableListOf<ScalyrEvent>()
        val messageNewRelicSdkList = mutableListOf<NewRelicBodySdk>()
        val messageNewRelicApiMap = mutableMapOf<String, NewRelicBodyApi>()
        val messageEmbraceList = mutableListOf<EmbraceBody>()

        // make the timestamp equals to timestamp when hit the api
        // convert the milli to nano, based on scalyr requirement.
        var counter = 0
        var ts: Long
        for (log in logs) {
            // to make sure each timestamp in each row is unique
            ts = log.timeStamp * 1000000
            ts += counter
            counter++
            var message = log.message
            if (decrypt != null) {
                message = decrypt.invoke(message)
            }
            val obj = JSONObject(message)
            val tagValue = obj.getString(Constants.TAG_LOG) ?: ""
            val priorityValue = obj.getString(Constants.PRIORITY_LOG).toIntOrNull()
                ?: 0
            val priorityName = when (priorityValue) {
                Constants.SEVERITY_HIGH -> LoggerReporting.P1
                Constants.SEVERITY_MEDIUM -> LoggerReporting.P2
                else -> ""
            }
            val tagMapsValue =
                StringBuilder(priorityName).append(LoggerReporting.DELIMITER_TAG_MAPS)
                    .append(tagValue).toString()
            LoggerReporting.getInstance().tagMapsScalyr[tagMapsValue]?.let {
                scalyrEventList.add(ScalyrEvent(ts, ScalyrEventAttrs(truncate(message))))
            }

            setMessageNewRelicList(tagMapsValue, message, messageNewRelicSdkList, messageNewRelicApiMap, log)

            LoggerReporting.getInstance().tagMapsEmbrace[tagMapsValue]?.let {
                messageEmbraceList.add(EmbraceBody(tagValue, jsonToMap(message)))
            }
        }
        return LoggerCloudModelWrapper(scalyrEventList, messageNewRelicSdkList, messageNewRelicApiMap, messageEmbraceList)
    }

    // P1#GP = key_new_relic_android, table_sf,
    private fun setMessageNewRelicList(
        tagMapsValue: String,
        message: String,
        messageNewRelicSdkList: MutableList<NewRelicBodySdk>,
        messageNewRelicApiMaps: MutableMap<String, NewRelicBodyApi>,
        log: Logger
    ) {
        LoggerReporting.getInstance().tagMapsNewRelic[tagMapsValue]?.let {
            val eventType = LoggerReporting.getInstance().tagMapsNrTable[it.newRelicTable] ?: Constants.EVENT_ANDROID_NEW_RELIC
            val nrConfig = LoggerReporting.getInstance().tagMapsNrKey[it.newRelicKey]

            if (nrConfig == null) {
                messageNewRelicSdkList.add(NewRelicBodySdk(eventType, jsonToMap(message)))
            } else {
                decryptNrKey?.let { decrypt ->
                    val msgEventNr = addEventNewRelic(message, eventType)
                    messageNewRelicApiMaps.addValue(
                        nrConfig.userId,
                        msgEventNr,
                        nrConfig.token,
                        decrypt
                    ) {
                        launch {
                            deleteLog(log)
                        }
                    }
                }
            }
        }
    }

    private suspend fun sendNewRelicSdkLogToServer(
        logs: List<Logger>,
        messageList: List<NewRelicBodySdk>
    ): Boolean {
        if (logs.isEmpty()) {
            return false
        }

        return loggerCloudNewRelicSdkImpl.sendToLogServer(messageList)
    }

    private suspend fun sendNewRelicApiLogToServer(
        messageMap: Map<String, NewRelicBodyApi>,
        logs: List<Logger>
    ): Boolean {
        if (logs.isEmpty()) {
            return false
        }

        val msgJobList = mutableListOf<Deferred<Boolean>>()

        messageMap.forEach { (_, value) ->
            val job = coroutineScope {
                async {
                    loggerCloudNewRelicApiImpl.sendToLogServer(
                        gson,
                        value.newRelicConfig,
                        value.messageList
                    )
                }
            }
            msgJobList.add(job)
        }
        return msgJobList.toList().awaitAll().any { it }
    }

    private suspend fun sendEmbraceLogToServer(
        logs: List<Logger>,
        embraceBodyList: List<EmbraceBody>
    ): Boolean {
        if (logs.isEmpty()) {
            return false
        }
        return loggerCloudEmbraceImpl.sendToLogServer(embraceBodyList)
    }

    fun truncate(str: String): String {
        return if (str.length > Constants.MAX_BUFFER) {
            str.substring(0, Constants.MAX_BUFFER)
        } else {
            str
        }
    }

    private fun addEventNewRelic(message: String, eventType: String): String {
        val gson = gson.fromJson(message, JsonObject::class.java)
        gson.addProperty(Constants.EVENT_TYPE_NEW_RELIC, eventType)
        if (eventType == Constants.EVENT_ANDROID_SF_NEW_RELIC) {
            gson.remove(Constants.PRIORITY_LOG)
        }
        return gson.toString()
    }

    private fun jsonToMap(message: String): HashMap<String, Any> {
        return gson.fromJson(message, object : TypeToken<HashMap<String, Any>>() {}.type)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
