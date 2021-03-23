package com.tokopedia.logger.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.BaseEncryptor
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicImpl
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.model.scalyr.ScalyrEvent
import com.tokopedia.logger.model.scalyr.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.TimberReportingTree
import kotlinx.coroutines.*
import javax.crypto.SecretKey
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class LoggerRepository(private val logDao: LoggerDao,
                       private val loggerCloudScalyrDataSource: LoggerCloudDataSource,
                       private val loggerCloudNewRelicImpl: LoggerCloudNewRelicImpl,
                       private val dispatcher: CoroutineDispatchers,
                       private val scalyrConfigs: List<ScalyrConfig>,
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
        sendLogToServer(Constants.SEVERITY_HIGH, logDao.getServerChannel(TimberReportingTree.P1, queryLimits[0]))
        sendLogToServer(Constants.SEVERITY_MEDIUM, logDao.getServerChannel(TimberReportingTree.P2, queryLimits[1]))
    }

    private suspend fun sendLogToServer(priority: Int, logs: List<Logger>) {
        val tokenIndex = priority - 1

        launch(context = dispatcher.io, block = {
            val scalyrSendSuccess = async { sendScalyrLogToServer(scalyrConfigs[tokenIndex], logs) }
            val newRelicSendSuccess = async { sendNewRelicLogToServer(logs) }
            if (scalyrSendSuccess.await() || newRelicSendSuccess.await()) {
                deleteEntries(logs)
            }
        })
    }

    suspend fun sendScalyrLogToServer(config: ScalyrConfig, logs: List<Logger>): Boolean {
        if (logs.isEmpty()) {
            return true
        }
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
        return loggerCloudScalyrDataSource.sendLogToServer(config, scalyrEventList)
    }

    suspend fun sendNewRelicLogToServer(logs: List<Logger>): Boolean {
        if (logs.isEmpty()) {
            return true
        }

        val messageList = mutableListOf<String>()
        for (log in logs) {
            val  message = encryptor.decrypt(log.message, secretKey)
            messageList.add(addEventNewRelic(message))
        }

        return loggerCloudNewRelicImpl.sendToLogServer(messageList)
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
        get() = dispatcher.io
}