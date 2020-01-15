package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import javax.crypto.SecretKey

class LoggerRepository(private val logDao: LoggerDao, private val server: LoggerCloudDatasource): LoggerRepositoryContract {

    override suspend fun insert(logger: Logger){
        logDao.insert(logger)
    }

    override suspend fun getCount(): Int{
        return logDao.getCountAll()
    }

    override suspend fun getHighPostPrio(entries: Int): List<Logger> {
        return logDao.getHighPostPrio(entries)
    }

    override suspend fun getLowPostPrio(entries: Int): List<Logger>{
        return logDao.getLowPostPrio(entries)
    }

    override suspend fun deleteEntry(timeStamp: Long){
        logDao.deleteEntry(timeStamp)
    }

    override suspend fun deleteExpiredData(timeStamp: Long) {
        logDao.deleteExpiredData(timeStamp)
    }

    override suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, secretKey: SecretKey): Int {
        return server.sendLogToServer(serverSeverity, TOKEN, logger, secretKey)
    }

    override suspend fun deleteExpiredHighPrio(timeStamp: Long) {
        logDao.deleteExpiredHighPrio(timeStamp)
    }

    override suspend fun deleteExpiredLowPrio(timeStamp: Long) {
        logDao.deleteExpiredLowPrio(timeStamp)
    }
}