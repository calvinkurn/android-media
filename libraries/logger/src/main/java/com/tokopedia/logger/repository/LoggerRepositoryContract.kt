package com.tokopedia.logger.repository

import android.util.Log
import com.tokopedia.logger.datasource.db.Logger
import javax.crypto.SecretKey

interface LoggerRepositoryContract {
    suspend fun insert(logger: Logger)
    suspend fun getFirst(entries: Int): List<Logger>
    suspend fun deleteBeforeTs(timeStamp: Long)
    suspend fun getCount(): Int
    suspend fun deleteAll()
    suspend fun getAll(): List<Logger>
    suspend fun getFirstHighPrio(entries: Int): List<Logger>
    suspend fun getFirstLowPrio(entries: Int): List<Logger>
    suspend fun getCountHighPrio(): Int
    suspend fun getCountLowPrio(): Int
    suspend fun deleteFirst()
    suspend fun deleteEntry(timeStamp: Long)
    suspend fun deleteHighPrioBeforeTs(timeStamp: Long)
    suspend fun deleteLowPrioBeforeTs(timeStamp: Long)
    suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, secretKey: SecretKey): Int
}