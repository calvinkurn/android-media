package com.tokopedia.logger

import android.app.Application
import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.keys.Keys.AUTH_NEW_RELIC_API_KEY
import com.tokopedia.keys.Keys.AUTH_SCALYR_API_KEY
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.LogWorker
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.LoggerUtils.getLogSession
import java.util.*

/**
 * Class to wrap the mechanism to send the logging message to server.
 * For the current implementation, this class is wrapping the client log app
 *
 * To Initialize:
 * LogManager.init(application);
 *
 * To send message to server:
 * LogManager.log(serverSeverity, priority, message)
 */
class LogManager(val application: Application) {

    companion object {

        private const val PRIORITY_LENGTH = 2

        @JvmStatic
        var scalyrConfigList = mutableListOf<ScalyrConfig>()

        @JvmStatic
        var newRelicConfigList = mutableListOf<NewRelicConfig>()

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
                val loggerCloudScalyrDataSource = LoggerCloudScalyrDataSource()
                val loggerCloudNewRelicDataSource = LoggerCloudNewRelicDataSource()
                val encryptor = AESEncryptorECB()
                val secretKey = encryptor.generateKey(Constants.ENCRYPTION_KEY)
                loggerRepository = LoggerRepository(logsDao,
                        loggerCloudScalyrDataSource,
                        loggerCloudNewRelicDataSource,
                        scalyrConfigList,
                        newRelicConfigList,
                        encryptor, secretKey)
            }
            return loggerRepository
        }

        @JvmStatic
        fun init(application: Application) {
            instance = LogManager(application)
        }

        /**
         * Setter for Scalyr Config List to be used checking when sent to server
         */
        fun setScalyrConfigList(): List<ScalyrConfig>? {
            if (scalyrConfigList.isNullOrEmpty()) {
                val context = instance?.application?.applicationContext
                for (i in 0 until PRIORITY_LENGTH) {
                    context?.let { scalyrConfigList.add(getScalyrConfig(it, i + 1)) }
                }
            }
            return scalyrConfigList
        }

        private fun getScalyrConfig(context: Context, priority: Int): ScalyrConfig {
            val session = getLogSession(context)
            val serverHost = String.format("android-main-app-p%s", priority)
            val parser = "android-parser"
            val installer: String = if (context.packageManager.getInstallerPackageName(context.packageName) != null) {
                context.packageManager?.getInstallerPackageName(context.packageName).toString()
            } else {
                ""
            }
            return ScalyrConfig(AUTH_SCALYR_API_KEY, session, serverHost, parser, context.packageName,
                    installer,
                    GlobalConfig.DEBUG, priority)
        }

        /**
         * Setter for New Relic Config List to be used checking when sent to server
         */
        fun setNewRelicConfigList(): List<NewRelicConfig>? {
            if (newRelicConfigList.isNullOrEmpty()) {
                val context = instance?.application?.applicationContext
                for (i in 0 until PRIORITY_LENGTH) {
                    context?.let { newRelicConfigList.add(getNewRelicConfig(it, i + 1)) }
                }
            }
            return newRelicConfigList
        }

        private fun getNewRelicConfig(context: Context, priority: Int): NewRelicConfig {
            val installer: String = if (context.packageManager.getInstallerPackageName(context.packageName) != null) {
                context.packageManager?.getInstallerPackageName(context.packageName).toString()
            } else {
                ""
            }
            return NewRelicConfig(AUTH_NEW_RELIC_API_KEY, context.packageName, installer, GlobalConfig.DEBUG, priority)
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