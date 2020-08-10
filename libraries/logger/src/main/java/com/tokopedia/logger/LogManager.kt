package com.tokopedia.logger

import android.app.Application
import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.logger.datasource.cloud.LoggerCloudLogentriesDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.model.ScalyrConfig
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.LogWorker
import com.tokopedia.logger.utils.Constants

/**
 * Class to wrap the mechanism to send the logging message to server.
 * For the current implementation, this class is wrapping the insight7 (Logentries)
 *
 * To Initialize:
 * LogManager.init(application);
 *
 * To send message to server:
 * LogManager.log(serverSeverity, priority, message)
 */
class LogManager(val application: Application) {

    companion object {
        @JvmStatic
        var logentriesToken: Array<String> = arrayOf()

        @JvmStatic
        var scalyrConfigList: List<ScalyrConfig> = mutableListOf()
        var scalyrEnabled: Boolean = false
        var logentriesEnabled: Boolean = true
        var isPrimaryLogentries: Boolean = true
        var isPrimaryScalyr: Boolean = false
        var queryLimits: List<Int> = mutableListOf(5, 5)

        @JvmField
        var instance: LogManager? = null
        lateinit var loggerRepository: LoggerRepository

        @JvmStatic
        fun getLogger(): LoggerRepository? {
            if (!::loggerRepository.isInitialized) {
                val instance = instance ?: return null
                val context = instance.application.applicationContext
                val logsDao = LoggerRoomDatabase.getDatabase(context).logDao()
                val loggerCloudLogentriesDataSource = LoggerCloudLogentriesDataSource()
                val loggerCloudScalyrDataSource = LoggerCloudScalyrDataSource()
                val encryptor = AESEncryptorECB()
                val secretKey = encryptor.generateKey(Constants.ENCRYPTION_KEY)
                loggerRepository = LoggerRepository(logsDao,
                        loggerCloudLogentriesDataSource, loggerCloudScalyrDataSource,
                        logentriesToken, scalyrConfigList,
                        encryptor, secretKey)
            }
            return loggerRepository
        }

        @JvmStatic
        fun init(application: Application) {
            instance = LogManager(application)
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Logger.ERROR, Logger.WARNING
         */
        suspend fun log(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            instance?.run {
                sendLogToDB(message, timeStamp, priority, serverChannel)
            }
        }

        private suspend fun sendLogToDB(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            getLogger()?.let { logger ->
                val truncatedMessage: String = if (message.length > Constants.MAX_BUFFER) {
                    message.substring(0, Constants.MAX_BUFFER)
                } else {
                    message
                }
                val log = Logger(timeStamp, serverChannel, priority, truncatedMessage)
                logger.insert(log)
                instance?.run {
                    LogWorker.scheduleWorker(this.application)
                }
            }
        }

        suspend fun sendLogToServer() {
            getLogger()?.sendLogToServer(queryLimits)
        }

        suspend fun deleteExpiredLogs() {
            getLogger()?.deleteExpiredData()
        }
    }
}