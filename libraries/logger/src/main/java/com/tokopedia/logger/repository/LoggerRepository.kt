package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import javax.crypto.SecretKey

class LoggerRepository(private val logDao: LoggerDao, private val server: LoggerCloudDatasource): LoggerRepositoryContract {

    override suspend fun insert(logger: Logger){
        logDao.insert(logger)
    }

    override suspend fun getFirst(entries: Int): List<Logger>{
        return logDao.getFirst(entries)
    }

    override suspend fun deleteBeforeTs(timeStamp: Long){
        logDao.deleteBeforeTs(timeStamp)
    }

    override suspend fun getCount(): Int{
        return logDao.getCountAll()
    }

    override suspend fun deleteAll(){
        logDao.deleteAll()
    }

    override suspend fun getAll(): List<Logger>{
        return logDao.getAll()
    }

    override suspend fun getFirstHighPrio(entries: Int): List<Logger>{
        return logDao.getFirstHighPrio(entries)
    }

    override suspend fun getFirstLowPrio(entries: Int): List<Logger>{
        return logDao.getFirstLowPrio(entries)
    }

    override suspend fun getCountHighPrio(): Int{
        return logDao.getCountHighPrio()
    }

    override suspend fun getCountLowPrio(): Int{
        return logDao.getCountLowPrio()
    }

    override suspend fun deleteFirst(){
        logDao.deleteFirst()
    }

    override suspend fun deleteEntry(timeStamp: Long){
        logDao.deleteEntry(timeStamp)
    }

    override suspend fun deleteHighPrioBeforeTs(timeStamp: Long){
        logDao.deleteHighPrioBeforeTs(timeStamp)
    }

    override suspend fun deleteLowPrioBeforeTs(timeStamp: Long){
        logDao.deleteLowPrioBeforeTs(timeStamp)
    }

    override suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, secretKey: SecretKey): Int {
        return server.sendLogToServer(serverSeverity, TOKEN, logger, secretKey)
    }
}