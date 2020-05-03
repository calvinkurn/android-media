package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger

interface LoggerRepositoryContract {
    suspend fun insert(logger: Logger)
    suspend fun getCount(): Int
    suspend fun getHighPostPrio(entries: Int): List<Logger>
    suspend fun getLowPostPrio(entries: Int): List<Logger>
    suspend fun deleteEntry(timeStamp: Long)
    suspend fun deleteEntries(loggers: List<Logger>)
    suspend fun deleteExpiredData()

    suspend fun sendLogToServer()
}