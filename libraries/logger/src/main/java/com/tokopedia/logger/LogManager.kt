package com.tokopedia.logger

import android.app.Application
import android.content.Context
import com.google.gson.Gson
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
import com.tokopedia.logger.utils.DataLogConfig
import com.tokopedia.logger.utils.LoggerReporting
import com.tokopedia.logger.utils.LoggerUtils.getLogSession
import com.tokopedia.logger.utils.LoggerUtils.getPartDeviceId

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
            loggerReporting.versionName = GlobalConfig.RAW_VERSION_NAME
            loggerReporting.versionCode = GlobalConfig.VERSION_CODE
            val installer: String? = application.packageManager.getInstallerPackageName(application.packageName)
            loggerReporting.installer = installer
            loggerReporting.packageName = application.packageName
            loggerReporting.debug = GlobalConfig.DEBUG
            refreshConfig()
        } catch (throwable: Throwable) {
            // do nothing
        }
    }

    fun refreshConfig(){
        try {
            val loggerReporting = LoggerReporting.getInstance()
            val logScalyrConfigString: String = loggerProxy.scalyrConfig
            val logNewRelicConfigString: String = loggerProxy.newRelicConfig
            if (logScalyrConfigString.isNotEmpty()) {
                val dataLogConfigScalyr = Gson().fromJson(logScalyrConfigString, DataLogConfig::class.java)
                if (dataLogConfigScalyr != null && dataLogConfigScalyr.isEnabled && GlobalConfig.VERSION_CODE >= dataLogConfigScalyr.appVersionMin && dataLogConfigScalyr.tags != null) {
                    loggerReporting.setQueryLimits(dataLogConfigScalyr.queryLimits)
                    loggerReporting.setPopulateTagMapsScalyr(dataLogConfigScalyr.tags)
                }
            }
            if (logNewRelicConfigString.isNotEmpty()) {
                val dataLogConfigNewRelic = Gson().fromJson(logNewRelicConfigString, DataLogConfig::class.java)
                if (dataLogConfigNewRelic != null && dataLogConfigNewRelic.tags != null &&
                        dataLogConfigNewRelic.isEnabled && GlobalConfig.VERSION_CODE >= dataLogConfigNewRelic.appVersionMin) {
                    loggerReporting.setPopulateTagMapsNewRelic(dataLogConfigNewRelic.tags)
                    loggerReporting.setQueryLimits(dataLogConfigNewRelic.queryLimits)
                }
            }
        } catch (e:Exception) {

        }
    }

    var loggerRepository: LoggerRepository? = null

    fun getLogger(): LoggerRepository {
        val loggerRepo = loggerRepository
        if (loggerRepo == null) {
            val context = application.applicationContext
            val logsDao = LoggerRoomDatabase.getDatabase(context).logDao()
            val loggerCloudScalyrDataSource = LoggerCloudScalyrDataSource()
            val loggerCloudNewRelicDataSource = LoggerCloudNewRelicDataSource()
            val encryptor = AESEncryptorECB()
            val secretKey = encryptor.generateKey(Constants.ENCRYPTION_KEY)
            val loggerRepoNew = LoggerRepository(logsDao,
                    loggerCloudScalyrDataSource,
                    loggerCloudNewRelicDataSource,
                    getScalyrConfigList(context),
                    NewRelicConfig(AUTH_NEW_RELIC_API_KEY),
                    encryptor, secretKey)
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
        val serverHost = String.format("android-main-app-p%s", priority)
        val parser = "android-parser"
        return ScalyrConfig(AUTH_SCALYR_API_KEY, session, serverHost, parser)
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

        /**
         * To give message log to logging server
         * logPriority to be handled are: Logger.ERROR, Logger.WARNING
         */
        suspend fun log(message: String, timeStamp: Long, priority: Int, serverChannel: String) {
            instance?.getLogger()?.let { logger ->
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
            instance?.getLogger()?.sendLogToServer(queryLimits)
        }

        suspend fun deleteExpiredLogs() {
            instance?.getLogger()?.deleteExpiredData()
        }
    }
}

interface LoggerProxy {
    val userId: String
    val scalyrConfig: String
    val newRelicConfig: String
}