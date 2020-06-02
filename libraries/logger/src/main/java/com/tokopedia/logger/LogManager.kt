package com.tokopedia.logger

import android.app.Application
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.logger.datasource.cloud.LoggerCloudLogentriesDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.model.ScalyrConfig
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.ServerJobService
import com.tokopedia.logger.service.ServerService
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.globalScopeLaunch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

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
class LogManager(val application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + handler

    private val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, _ -> }
    }

    companion object {
        @JvmStatic
        var logentriesToken: Array<String> = arrayOf()
        @JvmStatic
        var scalyrConfigList: List<ScalyrConfig> = mutableListOf()
        var scalyrEnabled: Boolean = false
        var logentriesEnabled: Boolean = true
        var isPrimaryLogentries: Boolean = true
        var isPrimaryScalyr: Boolean = false
        var queryLimits: List<Int> = mutableListOf(5,5)

        @JvmField
        var instance: LogManager? = null
        lateinit var loggerRepository: LoggerRepository
        private lateinit var pi: PendingIntent
        private lateinit var jobScheduler: JobScheduler
        private lateinit var jobInfo: JobInfo

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
            // Because JobService requires a minimum SDK version of 21, this if else block will allow devices with
            // SDK version lower than 21 to run a Service instead
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                val serviceComponent = ComponentName(application, ServerJobService::class.java)
                jobScheduler = application.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                jobInfo = JobInfo.Builder(1000, serviceComponent)
                    .setMinimumLatency(Constants.LOG_SERVICE_MIN_LATENCY)
                    .setOverrideDeadline(Constants.LOG_SERVICE_MAX_LATENCY)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build()
            } else {
                val intent = Intent(application, ServerService::class.java)
                pi = PendingIntent.getService(application, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            }
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Logger.ERROR, Logger.WARNING
         */
        fun log(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            instance?.run {
                sendLogToDB(message, timeStamp, priority, serverChannel)
                runService()
            }
        }

        private fun runService() {
            globalScopeLaunch({
                if (android.os.Build.VERSION.SDK_INT > 21
                        && ::jobScheduler.isInitialized && ::jobInfo.isInitialized) {
                    jobScheduler.schedule(jobInfo)
                } else if (::pi.isInitialized) {
                    pi.send()
                }
            })
        }

        private fun sendLogToDB(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            globalScopeLaunch({
                getLogger()?.let { logger ->
                    val truncatedMessage: String = if (message.length > Constants.MAX_BUFFER) {
                        message.substring(0, Constants.MAX_BUFFER)
                    } else {
                        message
                    }
                    val log = Logger(timeStamp, serverChannel, priority, truncatedMessage)
                    logger.insert(log)
                }
            })
        }

        fun sendLogToServer() {
            globalScopeLaunch({
                getLogger()?.sendLogToServer(queryLimits)
            })
        }

        fun deleteExpiredLogs() {
            globalScopeLaunch({
                getLogger()?.deleteExpiredData()
            })
        }
    }
}