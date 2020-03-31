package com.tokopedia.logger.repository

import com.tokopedia.encryption.security.BaseEncryptor
import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.ScalyrEvent
import com.tokopedia.logger.model.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.coroutineScope
import javax.crypto.SecretKey

class LoggerRepository(private val logDao: LoggerDao,
                       private val server: LoggerCloudDatasource,
                       private val scalyrLogger: LoggerCloudScalyrDataSource,
                       private val encryptor: BaseEncryptor,
                       private val secretKey: SecretKey) : LoggerRepositoryContract {

    override suspend fun insert(logger: Logger) {
        val encryptedLogger = logger.copy(message = encryptor.encrypt(logger.message, secretKey))
        logDao.insert(encryptedLogger)
    }

    override suspend fun getCount(): Int {
        return logDao.getCountAll()
    }

    override suspend fun getHighPostPrio(entries: Int): List<Logger> {
        return logDao.getHighPostPrio(entries)
    }

    override suspend fun getLowPostPrio(entries: Int): List<Logger> {
        return logDao.getLowPostPrio(entries)
    }

    override suspend fun deleteEntry(timeStamp: Long) {
        logDao.deleteEntry(timeStamp)
    }

    override suspend fun deleteEntries(loggers: List<Logger>) {
        logDao.deleteEntries(loggers)
    }

    override suspend fun deleteExpiredData(timeStamp: Long) {
        logDao.deleteExpiredData(timeStamp)
    }

    override suspend fun sendLogToServer(serverSeverity: Int,
                                         TOKEN: Array<String>,
                                         logger: Logger): Int = coroutineScope {
        val message = encryptor.decrypt(logger.message, secretKey)
        server.sendLogToServer(serverSeverity, TOKEN, truncate(message))
    }

    override suspend fun sendScalyrLogToServer(logs: List<Logger>) = coroutineScope {
        val scalyrEventList = mutableListOf<ScalyrEvent>()
        //make the timestamp equals to timestamp when hit the api
        //covnert the milli to nano, based on scalyr requirement.
        var ts = System.currentTimeMillis() * 1000000
        for (log in logs) {
            //to make sure each timestamp in each row is unique
            ts += 1000
            val message = encryptor.decrypt(log.message, secretKey)
            scalyrEventList.add(ScalyrEvent(ts, ScalyrEventAttrs(truncate(message))))
        }
        scalyrLogger.sendLogToServer(scalyrEventList)
    }

    fun truncate (str:String):String {
        return if (str.length > Constants.MAX_BUFFER) {
            str.substring(0, Constants.MAX_BUFFER)
        } else {
            str
        }
    }

    override suspend fun deleteExpiredHighPrio(timeStamp: Long) {
        logDao.deleteExpiredHighPrio(timeStamp)
    }

    override suspend fun deleteExpiredLowPrio(timeStamp: Long) {
        logDao.deleteExpiredLowPrio(timeStamp)
    }
}