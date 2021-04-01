package com.tokopedia.logger.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.encryption.security.BaseEncryptor
import com.tokopedia.logger.LogManager
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
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.Tag
import kotlinx.coroutines.*
import java.lang.StringBuilder
import javax.crypto.SecretKey
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class LoggerRepository(private val logDao: LoggerDao,
                       private val loggerCloudScalyrDataSource: LoggerCloudDataSource,
                       private val loggerCloudNewRelicImpl: LoggerCloudNewRelicImpl,
                       private val scalyrConfigs: List<ScalyrConfig>,
                       private val newRelicConfigs: List<NewRelicConfig>,
                       private val encryptor: BaseEncryptor,
                       private val secretKey: SecretKey) : LoggerRepositoryContract, CoroutineScope {


    override suspend fun insert(logger: Logger) {
        val encryptedLogger = logger.copy(message = encryptor.encrypt(logger.message, secretKey))
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

        val scalyrEventList = setScalyrEventList(logs)
        val newRelicConfigList = setNewRelicConfigList(logs)

//        if (scalyrConfigs.isNotEmpty() && newRelicConfigs.isEmpty()) {
//            val scalyrSendSuccess = sendScalyrLogToServer(scalyrConfigs[tokenIndex], logs)
//            if (scalyrSendSuccess) {
//                deleteEntries(logs)
//            }
//        } else if (scalyrConfigs.isEmpty() && newRelicConfigs.isNotEmpty()) {
//            val newRelicSendSuccess = sendNewRelicLogToServer(newRelicConfigs[tokenIndex], logs)
//            if (newRelicSendSuccess) {
//                deleteEntries(logs)
//            }
//        } else if (scalyrConfigs.isNotEmpty() && newRelicConfigs.isNotEmpty()) {
//            sendMultipleLogToServer(tokenIndex, logs)
//        }

        coroutineScope {
            launch {
                val jobList = mutableListOf<Deferred<Boolean>>()
                if (isExistTagMapsScalyr(scalyrEventList)) {
                    val jobScalyr = async { sendScalyrLogToServer(scalyrConfigs[tokenIndex], logs, scalyrEventList) }
                    jobList.add(jobScalyr)
                }

                if (isExistTagMapsNewRelic(newRelicConfigList)) {
                    val jobNewRelic = async { sendNewRelicLogToServer(newRelicConfigs[tokenIndex], logs, newRelicConfigList) }
                    jobList.add(jobNewRelic)
                }

                jobList.awaitAll().forEach {
                    if (it) {
                        deleteEntries(logs)
                    }
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

    private fun setScalyrEventList(logs: List<Logger>): List<ScalyrEvent> {
        val scalyrEventList = mutableListOf<ScalyrEvent>()
        //make the timestamp equals to timestamp when hit the api
        //convert the milli to nano, based on scalyr requirement.
        var counter = 0
        var ts: Long
        for (log in logs) {
            //to make sure each timestamp in each row is unique
            ts = log.timeStamp * 1000000
            ts += counter
            counter++
            val message = encryptor.decrypt(log.message, secretKey)
            scalyrEventList.add(ScalyrEvent(ts, ScalyrEventAttrs(truncate(message))))
        }
        return scalyrEventList
    }

    private fun isExistTagMapsScalyr(scalyrEventList: List<ScalyrEvent>): Boolean {
        for (scalyrEvent in scalyrEventList) {
            val tagValue = jsonStringToMap(scalyrEvent.attrs.message).get(Constants.TAG_LOG)
                    ?: ""
            val priorityValue = jsonStringToMap(scalyrEvent.attrs.message).get(Constants.PRIORITY_LOG)?.toIntOrNull()
                    ?: 0
            val priorityName = when (priorityValue) {
                Constants.SEVERITY_HIGH -> LoggerReporting.P1
                Constants.SEVERITY_MEDIUM -> LoggerReporting.P2
                else -> ""
            }
            val tagMapsValue = StringBuilder(priorityName).append(LoggerReporting.DELIMITER_TAG_MAPS).append(tagValue).toString()
            LoggerReporting.getInstance().tagMapsScalyr[tagMapsValue]?.let {
                return true
            }
        }
        return false
    }

    private fun isExistTagMapsNewRelic(messageList: List<String>): Boolean {
        for (message in messageList) {
            val tagValue = jsonStringToMap(message).get(Constants.TAG_LOG) ?: ""
            val priorityValue = jsonStringToMap(message).get(Constants.PRIORITY_LOG)?.toIntOrNull()
                    ?: 0
            val priorityName = when (priorityValue) {
                Constants.SEVERITY_HIGH -> LoggerReporting.P1
                Constants.SEVERITY_MEDIUM -> LoggerReporting.P2
                else -> ""
            }
            val tagMapsValue = StringBuilder(priorityName).append(LoggerReporting.DELIMITER_TAG_MAPS).append(tagValue).toString()
            LoggerReporting.getInstance().tagMapsNewRelic[tagMapsValue]?.let {
                return true
            }
        }
        return false
    }

    suspend fun sendNewRelicLogToServer(config: NewRelicConfig, logs: List<Logger>, messageList: List<String>): Boolean {
        if (logs.isEmpty()) {
            return false
        }

        return loggerCloudNewRelicImpl.sendToLogServer(config, messageList)
    }

    private fun setNewRelicConfigList(logs: List<Logger>): List<String> {
        val messageList = mutableListOf<String>()

        for (log in logs) {
            val message = encryptor.decrypt(log.message, secretKey)
            messageList.add(addEventNewRelic(message))
        }
        return messageList
    }

    private fun addEventNewRelic(message: String): String {
        val gson = Gson().fromJson(message, JsonObject::class.java)
        gson.addProperty(Constants.EVENT_TYPE_NEW_RELIC, Constants.EVENT_ANDROID_NEW_RELIC)
        return gson.toString()
    }

    private fun jsonStringToMap(message: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(message, type)
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