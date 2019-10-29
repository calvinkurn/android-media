package com.tokopedia.logger

import android.app.Application
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.datasource.cloud.LoggerCloudDatasource
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.ServerJobService
import com.tokopedia.logger.service.ServerService
import com.tokopedia.logger.utils.*
import kotlinx.coroutines.*
import java.lang.Exception
import javax.crypto.SecretKey
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

    private suspend fun sendLogToDB(message: String, timeStamp: Long, priority: Int) {
        Log.e("toDB","Sending Log to DB")
        // Handle Message
        val truncatedMessage: String = if (message.length > Constants.MAX_BUFFER) {
            message.substring(0,Constants.MAX_BUFFER)
        } else {
            message
        }
        val encryptedMessage = encrypt(truncatedMessage, secretKey)
        val log = Logger(timeStamp,priority,encryptedMessage)
        loggerRepository.insert(log)
    }

    companion object {
        @JvmStatic
        var TOKEN: Array<String> = arrayOf()

        @JvmField
        var instance: LogManager? = null
        lateinit var loggerRepository: LoggerRepository
        private lateinit var intent: Intent
        private lateinit var pi: PendingIntent
        private lateinit var jobScheduler: JobScheduler
        private lateinit var jobInfo: JobInfo
        private lateinit var secretKey: SecretKey

        @JvmStatic
        fun init(application: Application) {
            instance = LogManager(application)
            val logsDao = LoggerRoomDatabase.getDatabase(application).logDao()
            val server = LoggerCloudDatasource()
            loggerRepository = LoggerRepository(logsDao,server)
            secretKey = generateKey(Constants.KEY)
            // Because JobService requires a minimum SDK version of 21, this if else block will allow devices with
            // SDK version lower than 21 to run a Service instead
            if (android.os.Build.VERSION.SDK_INT > 21){
                val serviceComponent = ComponentName(application, ServerJobService::class.java)
                jobScheduler = application.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                jobInfo = JobInfo.Builder(1000,serviceComponent)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build()
            }
            else{
                intent = Intent(application,ServerService::class.java)
                pi = PendingIntent.getService(application, 0, intent , PendingIntent.FLAG_CANCEL_CURRENT)
            }
        }

        /**
         * To give message log to logging server
         * logPriority to be handled are: Logger.ERROR, Logger.WARNING
         */

        @JvmStatic
        fun log(message: String, timeStamp: Long, priority:Int) {
            instance?.run {
                runBlocking {
                    sendLogToDB(message, timeStamp, priority)
                    if (android.os.Build.VERSION.SDK_INT > 21) {
                        jobScheduler.schedule(jobInfo)
                    } else {
                        pi.send()
                    }
                }
            }
        }

        // Functions Used in Service

        suspend fun sendLogToServer() {
            val nHighPrioLogs = 3
            val nLowPrioLogs = 2

            val highPrioLoggers: List<Logger> = loggerRepository.getFirstHighPrio(nHighPrioLogs)
            val lowPrioLoggers: List<Logger> = loggerRepository.getFirstLowPrio(nLowPrioLogs)
            var logs = highPrioLoggers.toMutableList()

            for (lowPrioLog in lowPrioLoggers) {
                logs.add(lowPrioLog)
            }

            for (log in logs) {
                val message = decrypt(log.message, secretKey)
                val ts = log.timeStamp
                val severity = setSeverity(message)
                if (severity != TimberReportingTree.NO_SEVERITY) {
                    val errorCode = loggerRepository.sendLogToServer(severity, TOKEN, log, secretKey)
                    if(errorCode == 204) {
                        loggerRepository.deleteEntry(ts)
                    }
                }

            }
        }

        suspend fun inspectLogs() {
            // Threshold can be set
            val currentTs = System.currentTimeMillis()
            loggerRepository.deleteHighPrioBeforeTs(currentTs - Constants.OFFLINE_TAG_THRESHOLD)
            loggerRepository.deleteLowPrioBeforeTs(currentTs - Constants.ONLINE_TAG_THRESHOLD)
        }

        suspend fun getCount(): Int{
            return loggerRepository.getCount()
        }

        private fun setSeverity(message: String): Int {
            return when {
                message.contains(TimberReportingTree.P1) -> TimberReportingTree.SEVERITY_P1_Testing
                message.contains(TimberReportingTree.P2) -> TimberReportingTree.SEVERITY_P2_Testing
                message.contains(TimberReportingTree.P3) -> TimberReportingTree.SEVERITY_LOW
                else -> TimberReportingTree.NO_SEVERITY
            }
        }
    }
}