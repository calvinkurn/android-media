package com.tokopedia.logger

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicApiDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicSdkDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudScalyrDataSource
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.repository.InternalLoggerInterface
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.service.LogWorker
import com.tokopedia.logger.utils.DataLogConfig
import com.tokopedia.logger.utils.LoggerReporting
import com.tokopedia.logger.utils.LoggerUtils.getLogSession
import com.tokopedia.logger.utils.LoggerUtils.getPartDeviceId
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.globalScopeLaunch

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
class LogManager(val application: Application, val loggerProxy: LoggerProxy) {

    init {
        try {
            val loggerReporting = LoggerReporting.getInstance()
            loggerReporting.partDeviceId = getPartDeviceId(application)
            loggerReporting.versionName = loggerProxy.versionName
            loggerReporting.versionCode = loggerProxy.versionCode
            val installer: String =
                application.packageManager.getInstallerPackageName(application.packageName)
                    ?: ""
            loggerReporting.installer = installer
            loggerReporting.packageName = application.packageName
            loggerReporting.debug = loggerProxy.isDebug
            refreshConfig()
        } catch (throwable: Throwable) {
            // do nothing
        }
    }

    fun refreshConfig() {
        try {
            val loggerReporting = LoggerReporting.getInstance()
            val logScalyrConfigString: String = loggerProxy.scalyrConfig
            val logNewRelicConfigString: String = loggerProxy.newRelicConfig
            if (logScalyrConfigString.isNotEmpty()) {
                val dataLogConfigScalyr =
                    Gson().fromJson(logScalyrConfigString, DataLogConfig::class.java)
                if (dataLogConfigScalyr != null && dataLogConfigScalyr.isEnabled && loggerProxy.versionCode >= dataLogConfigScalyr.appVersionMin && dataLogConfigScalyr.tags != null) {
                    val queryLimit = dataLogConfigScalyr.queryLimits
                    if (queryLimit != null) {
                        queryLimits = queryLimit
                    }
                    loggerReporting.setPopulateTagMapsScalyr(dataLogConfigScalyr.tags)
                }
            }
            if (logNewRelicConfigString.isNotEmpty()) {
                val dataLogConfigNewRelic =
                    Gson().fromJson(logNewRelicConfigString, DataLogConfig::class.java)
                if (dataLogConfigNewRelic != null && dataLogConfigNewRelic.tags != null &&
                    dataLogConfigNewRelic.isEnabled && loggerProxy.versionCode >= dataLogConfigNewRelic.appVersionMin
                ) {
                    val queryLimit = dataLogConfigNewRelic.queryLimits
                    if (queryLimit != null) {
                        queryLimits = queryLimit
                    }

                    if (dataLogConfigNewRelic.keys?.isNotEmpty() == true) {
                        loggerReporting.setPopulateKeyMapsNewRelic(dataLogConfigNewRelic.keys)
                    }

                    if (dataLogConfigNewRelic.tables?.isNotEmpty() == true) {
                        loggerReporting.setPopulateTableMapsNewRelic(dataLogConfigNewRelic.tables)
                    }

                    loggerReporting.setPopulateTagMapsNewRelic(dataLogConfigNewRelic.tags)
                }
            }
        } catch (e: Exception) {
        }
    }

    var loggerRepository: LoggerRepository? = null

    fun getLogger(): LoggerRepository {
        val loggerRepo = loggerRepository
        if (loggerRepo == null) {
            val context = application.applicationContext
            val logsDao = LoggerRoomDatabase.getDatabase(context).logDao()
            val loggerCloudScalyrDataSource = LoggerCloudScalyrDataSource()
            val loggerCloudNewRelicSdkDataSource = LoggerCloudNewRelicSdkDataSource()
            val loggerCloudNewRelicApiDataSource = LoggerCloudNewRelicApiDataSource()
            val loggerRepoNew = LoggerRepository(
                Gson(),
                logsDao,
                loggerCloudScalyrDataSource,
                loggerCloudNewRelicSdkDataSource,
                loggerCloudNewRelicApiDataSource,
                getScalyrConfigList(context),
                loggerProxy.encrypt,
                loggerProxy.decrypt,
                loggerProxy.decryptNrKey,
                loggerProxy.internalLogger
            )
            loggerRepository = loggerRepoNew
            return loggerRepoNew
        } else {
            return loggerRepo
        }
    }

    private fun getScalyrConfigList(context: Context): List<ScalyrConfig> {
        val scalyrConfigList = mutableListOf<ScalyrConfig>()
        for (i in 0 until PRIORITY_LENGTH) {
            scalyrConfigList.add(getScalyrConfig(context, i + 1))
        }
        return scalyrConfigList
    }

    private fun getScalyrConfig(context: Context, priority: Int): ScalyrConfig {
        val session = getLogSession(context)
        val serverHost = String.format(loggerProxy.parserScalyr, priority)
        val parser = "android-parser"
        return ScalyrConfig(loggerProxy.scalyrToken, session, serverHost, parser)
    }

    companion object {

        const val PRIORITY_LENGTH = 2

        var queryLimits: List<Int> = mutableListOf(5, 5)

        @JvmField
        var instance: LogManager? = null

        @JvmStatic
        fun init(application: Application, loggerProxy: LoggerProxy) {
            instance = LogManager(application, loggerProxy)
        }

        fun log(priority: Priority, tag: String, message: Map<String, String>) {
            globalScopeLaunch({
                val thisInstance = instance ?: return@globalScopeLaunch
                val processedLogger = LoggerReporting.getInstance().getProcessedMessage(
                    priority,
                    tag,
                    message,
                    thisInstance.loggerProxy.userId,
                    thisInstance.loggerProxy.activityName
                )
                if (processedLogger != null) {
                    instance?.getLogger()?.insert(processedLogger)
                    instance?.run {
                        LogWorker.scheduleWorker(this.application)
                    }
                }
            })
        }

        suspend fun sendLogToServer() {
            instance?.getLogger()?.sendLogToServer(queryLimits)
        }

        suspend fun deleteExpiredLogs() {
            instance?.getLogger()?.deleteExpiredData()
        }
    }
}

interface LoggerProxy {
    val userId: String
    val parserScalyr: String
    val scalyrConfig: String
    val newRelicConfig: String
    val isDebug: Boolean
    val newRelicToken: String
    val newRelicUserId: String
    val scalyrToken: String
    val versionCode: Int
    val versionName: String
    val encrypt: ((String) -> (String))?
    val decrypt: ((String) -> (String))?
    val decryptNrKey: ((String) -> (String))?
    val activityName: String
    val internalLogger: InternalLoggerInterface?
}
