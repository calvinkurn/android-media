package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger
import javax.crypto.SecretKey

interface LoggerRepositoryContract {
    suspend fun insert(logger: Logger)
    suspend fun getCount(): Int
    suspend fun getHighPostPrio(entries: Int): List<Logger>
    suspend fun getLowPostPrio(entries: Int): List<Logger>
    suspend fun deleteEntry(timeStamp: Long)
    suspend fun deleteExpiredData(timeStamp: Long)
    suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, secretKey: SecretKey): Int
    suspend fun deleteExpiredHighPrio(timeStamp: Long)
    suspend fun deleteExpiredLowPrio(timeStamp: Long)
}