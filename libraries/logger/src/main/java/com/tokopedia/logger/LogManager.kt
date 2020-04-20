package com.tokopedia.logger

import android.app.Application
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.ServerJobService
import com.tokopedia.logger.service.ServerService
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.TimberReportingTree
import com.tokopedia.logger.utils.globalScopeLaunch
import kotlinx.coroutines.*
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

    fun setLogEntriesToken(tokenList: Array<String>) {
        TOKEN = tokenList
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
        }
    }

    companion object {
        @JvmStatic
        var TOKEN: Array<String> = arrayOf()
        var scalyrEnabled: Boolean = false
        var logentriesEnabled: Boolean = true
        var isPrimaryLogentries: Boolean = true
        var isPrimaryScalyr: Boolean = false

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
                val server = LoggerCloudDatasource()
                val scalyrLogger = LoggerCloudScalyrDataSource(context)
                val encryptor = AESEncryptorECB()
                val secretKey = encryptor.generateKey(Constants.ENCRYPTION_KEY)
                loggerRepository = LoggerRepository(logsDao, server, scalyrLogger, encryptor, secretKey)
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

        @JvmStatic
        fun log(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            instance?.run {
                globalScopeLaunch({
                    sendLogToDB(message, timeStamp, priority, serverChannel)
                    runService()
                }, {
                    it.printStackTrace()
                }, {
                    return@globalScopeLaunch
                })
            }
        }

        private fun runService() {
            if (android.os.Build.VERSION.SDK_INT > 21) {
                jobScheduler.schedule(jobInfo)
            } else {
                pi.send()
            }
        }

        suspend fun sendLogToServer() = coroutineScope {
            getLogger()?.let { logger ->
                val highPriorityLoggers: List<Logger> = logger.getHighPostPrio(Constants.SEND_PRIORITY_OFFLINE)
                val lowPriorityLoggers: List<Logger> = logger.getLowPostPrio(Constants.SEND_PRIORITY_ONLINE)
                val logs = highPriorityLoggers.toMutableList()

                for (lowPriorityLogger in lowPriorityLoggers) {
                    logs.add(lowPriorityLogger)
                }

                var scalyrSuccessCode = Constants.LOG_DEFAULT_ERROR_CODE
                if (scalyrEnabled) {
                    try {
                        scalyrSuccessCode = logger.sendScalyrLogToServer(logs)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (logentriesEnabled) {
                    for (log in logs) {
                        val ts = log.timeStamp
                        val severity = getSeverity(log.serverChannel)
                        if (severity != Constants.SEVERITY_NONE) {
                            val errorCode = logger.sendLogToServer(severity, TOKEN, log)
                            if (isPrimaryLogentries) {
                                if (errorCode == Constants.LOGENTRIES_SUCCESS_CODE) {
                                    logger.deleteEntry(ts)
                                }
                            }
                            delay(100)
                        }
                    }
                }

                if (isPrimaryScalyr) {
                    if (scalyrSuccessCode == Constants.SCALYR_SUCCESS_CODE) {
                        logger.deleteEntries(logs)
                    }
                }
            }
        }

        suspend fun deleteExpiredLogs() {
            getLogger()?.run {
                val currentTimestamp = System.currentTimeMillis()
                deleteExpiredHighPrio(currentTimestamp - Constants.OFFLINE_TAG_THRESHOLD)
                deleteExpiredLowPrio(currentTimestamp - Constants.ONLINE_TAG_THRESHOLD)
            }
        }

        private fun getSeverity(serverChannel: String): Int {
            return when (serverChannel) {
                TimberReportingTree.P1 -> Constants.SEVERITY_HIGH
                TimberReportingTree.P2 -> Constants.SEVERITY_MEDIUM
                else -> Constants.SEVERITY_NONE
            }
        }
    }
}