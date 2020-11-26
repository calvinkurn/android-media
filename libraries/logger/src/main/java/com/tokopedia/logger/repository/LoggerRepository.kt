package com.tokopedia.logger.repository

import com.tokopedia.encryption.security.BaseEncryptor
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.ScalyrConfig
import com.tokopedia.logger.model.ScalyrEvent
import com.tokopedia.logger.model.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.TimberReportingTree
import kotlinx.coroutines.delay
import javax.crypto.SecretKey

class LoggerRepository(private val logDao: LoggerDao,
                       private val loggerCloudLogentriesDataSource: LoggerCloudDataSource<String, String>,
                       private val loggerCloudScalyrDataSource: LoggerCloudDataSource<ScalyrConfig, ScalyrEvent>,
                       private val logentriesToken: Array<String>,
                       private val scalyrConfigs: List<ScalyrConfig>,
                       private val encryptor: BaseEncryptor,
                       private val secretKey: SecretKey) : LoggerRepositoryContract {

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
        val tokenIndex = priority-1

        var scalyrSendSuccess = false
        if (LogManager.scalyrEnabled) {
            scalyrSendSuccess = sendScalyrLogToServer(scalyrConfigs[tokenIndex], logs)
        }

        if (LogManager.logentriesEnabled) {
            for (log in logs) {
                val success = sendLogentriesLogToServer(logentriesToken[tokenIndex], log)
                if (LogManager.isPrimaryLogentries && success) {
                    deleteEntry(log.timeStamp)
                }
                delay(100)
            }
        }

        if (LogManager.isPrimaryScalyr) {
            if (scalyrSendSuccess) {
                deleteEntries(logs)
            }
        }
    }

    suspend fun sendLogentriesLogToServer(token: String, logger: Logger): Boolean {
        val message = encryptor.decrypt(logger.message, secretKey)
        val messageArray = listOf(truncate(message))
        return loggerCloudLogentriesDataSource.sendLogToServer(token, messageArray)
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

    fun truncate (str:String):String {
        return if (str.length > Constants.MAX_BUFFER) {
            str.substring(0, Constants.MAX_BUFFER)
        } else {
            str
        }
    }
}