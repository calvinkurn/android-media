package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger

interface LoggerRepositoryContract {
    suspend fun insert(logger: Logger)
    suspend fun getCount(): Int
    suspend fun deleteEntry(timeStamp: Long)
    suspend fun deleteEntries(loggers: List<Logger>)
    suspend fun deleteLog(logger: Logger)
    suspend fun deleteExpiredData()

    suspend fun sendLogToServer(queryLimits: List<Int>)

    suspend fun getLoggerList(serverChannel: String, limit: Int, offset: Int): List<Logger>
    suspend fun deleteAll()
}
