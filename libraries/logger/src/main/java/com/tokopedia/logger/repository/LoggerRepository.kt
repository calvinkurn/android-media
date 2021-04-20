package com.tokopedia.logger.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicImpl
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.model.scalyr.ScalyrEvent
import com.tokopedia.logger.model.scalyr.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.LoggerReporting
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class LoggerRepository(private val logDao: LoggerDao,
                       private val loggerCloudScalyrDataSource: LoggerCloudDataSource,
                       private val loggerCloudNewRelicImpl: LoggerCloudNewRelicImpl,
                       private val scalyrConfigs: List<ScalyrConfig>,
                       private val newRelicConfig: NewRelicConfig,
                       private val encrypt: ((String) -> (String))? = null,
                       private val decrypt: ((String) -> (String))? = null) : LoggerRepositoryContract, CoroutineScope {

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

    override suspend fun deleteExpiredData() {
        val currentTimestamp = System.currentTimeMillis()
        logDao.deleteExpiredHighPrio(currentTimestamp - Constants.OFFLINE_TAG_THRESHOLD)
        logDao.deleteExpiredLowPrio(currentTimestamp - Constants.ONLINE_TAG_THRESHOLD)
    }

    override suspend fun sendLogToServer(queryLimits: List<Int>) {
        sendLogToServer(Constants.SEVERITY_HIGH, logDao.getServerChannel(LoggerReporting.P1, queryLimits[0]))
        sendLogToServer(Constants.SEVERITY_MEDIUM, logDao.getServerChannel(LoggerReporting.P2, queryLimits[1]))
    }

    private suspend fun sendLogToServer(priority: Int, logs: List<Logger>) {
        val tokenIndex = priority - 1

        coroutineScope {
            launch {
                val jobList = mutableListOf<Deferred<Boolean>>()

                val mappedList = mapLogs(logs)
                val scalyrEventListList = mappedList.first
                val newRelicConfigList = mappedList.second

                if (scalyrEventListList.isNotEmpty()) {
                    val jobScalyr = async { sendScalyrLogToServer(scalyrConfigs[tokenIndex], logs, scalyrEventListList) }
                    jobList.add(jobScalyr)
                }

                if (newRelicConfigList.isNotEmpty()) {
                    val jobNewRelic = async { sendNewRelicLogToServer(newRelicConfig, logs, newRelicConfigList) }
                    jobList.add(jobNewRelic)
                }

                val isSuccess = jobList.awaitAll().any { it }
                if (isSuccess) {
                    deleteEntries(logs)
                }
            }
        }
    }

    suspend fun sendScalyrLogToServer(config: ScalyrConfig, logs: List<Logger>, scalyrEventList: List<ScalyrEvent>): Boolean {
        if (logs.isEmpty()) {
            return true
        }

        return loggerCloudScalyrDataSource.sendLogToServer(config, scalyrEventList)
    }

    private fun mapLogs(logs: List<Logger>): Pair<List<ScalyrEvent>, List<String>> {
        val scalyrEventList = mutableListOf<ScalyrEvent>()
        val messageNewRelicList = mutableListOf<String>()
        //make the timestamp equals to timestamp when hit the api
        //convert the milli to nano, based on scalyr requirement.
        var counter = 0
        var ts: Long
        for (log in logs) {
            //to make sure each timestamp in each row is unique
            ts = log.timeStamp * 1000000
            ts += counter
            counter++
            var message = log.message
            if (decrypt!= null) {
                message = decrypt.invoke(message)
            }
            val obj = JSONObject(message)
            val tagValue = obj.getString (Constants.TAG_LOG) ?: ""
            val priorityValue = obj.getString(Constants.PRIORITY_LOG).toIntOrNull()
                    ?: 0
            val priorityName = when (priorityValue) {
                Constants.SEVERITY_HIGH -> LoggerReporting.P1
                Constants.SEVERITY_MEDIUM -> LoggerReporting.P2
                else -> ""
            }
            val tagMapsValue = StringBuilder(priorityName).append(LoggerReporting.DELIMITER_TAG_MAPS).append(tagValue).toString()
            LoggerReporting.getInstance().tagMapsScalyr[tagMapsValue]?.let {
                scalyrEventList.add(ScalyrEvent(ts, ScalyrEventAttrs(truncate(message))))
            }
            LoggerReporting.getInstance().tagMapsNewRelic[tagMapsValue]?.let {
                messageNewRelicList.add(addEventNewRelic(message))
            }
        }
        return Pair(scalyrEventList, messageNewRelicList)
    }

    suspend fun sendNewRelicLogToServer(config: NewRelicConfig, logs: List<Logger>, messageList: List<String>): Boolean {
        if (logs.isEmpty()) {
            return false
        }

        return loggerCloudNewRelicImpl.sendToLogServer(config, messageList)
    }

    private fun addEventNewRelic(message: String): String {
        val gson = Gson().fromJson(message, JsonObject::class.java)
        gson.addProperty(Constants.EVENT_TYPE_NEW_RELIC, Constants.EVENT_ANDROID_NEW_RELIC)
        return gson.toString()
    }

    fun truncate(str: String): String {
        return if (str.length > Constants.MAX_BUFFER) {
            str.substring(0, Constants.MAX_BUFFER)
        } else {
            str
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}