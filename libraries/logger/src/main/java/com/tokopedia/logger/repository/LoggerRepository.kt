package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.ScalyrEvent
import com.tokopedia.logger.model.ScalyrEventAttrs
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.decrypt
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import javax.crypto.SecretKey

class LoggerRepository(private val logDao: LoggerDao,
                       private val server: LoggerCloudDatasource,
                       private val scalyrLogger: LoggerCloudScalyrDataSource) : LoggerRepositoryContract {

    override suspend fun insert(logger: Logger) {
        logDao.insert(logger)
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

    override suspend fun deleteExpiredData(timeStamp: Long) {
        logDao.deleteExpiredData(timeStamp)
    }

    override suspend fun sendLogToServer(serverSeverity: Int,
                                         TOKEN: Array<String>,
                                         logger: Logger,
                                         secretKey: SecretKey): Int = coroutineScope {
        val message = decrypt(logger.message, secretKey)
        val truncatedMessage = if (message.length > Constants.MAX_BUFFER) {
            message.substring(0, Constants.MAX_BUFFER)
        } else {
            message
        }
        server.sendLogToServer(serverSeverity, TOKEN, truncatedMessage)
    }

    override suspend fun sendScalyrLogToServer(logs: List<Logger>,
                                               secretKey: SecretKey) = coroutineScope {
        val scalyrEventList = mutableListOf<ScalyrEvent>()
        for (log in logs) {
            val ts = log.timeStamp
            val message = decrypt(log.message, secretKey)
            val truncatedMessage = if (message.length > Constants.MAX_BUFFER) {
                message.substring(0, Constants.MAX_BUFFER)
            } else {
                message
            }
            scalyrEventList.add(ScalyrEvent(ts * 1000, ScalyrEventAttrs(truncatedMessage)))
        }
        scalyrLogger.sendLogToServer(scalyrEventList)
    }

    override suspend fun deleteExpiredHighPrio(timeStamp: Long) {
        logDao.deleteExpiredHighPrio(timeStamp)
    }

    override suspend fun deleteExpiredLowPrio(timeStamp: Long) {
        logDao.deleteExpiredLowPrio(timeStamp)
    }
}