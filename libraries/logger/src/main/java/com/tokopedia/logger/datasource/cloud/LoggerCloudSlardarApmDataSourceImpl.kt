package com.tokopedia.logger.datasource.cloud

import com.bytedance.apm.ApmAgent
import com.google.gson.Gson
import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class LoggerCloudSlardarApmDataSourceImpl: LoggerCloudSlardarApmDataSource {

    private val gson by lazy { Gson() }
    override suspend fun sendApmLogToServer(newRelicSdkList: List<NewRelicBodySdk>): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                newRelicSdkList.forEach {
                    val logString = gson.toJson(it)
                    val extraLog = JSONObject()
                    extraLog.put(JSON_LOG_KEY, logString)
                    ApmAgent.monitorEvent(SERVICE_NAME, null, null, extraLog)
                }
                true
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            false
        }
    }

    override suspend fun sendApmLogToServer(newRelicApiMap: Map<String, NewRelicBodyApi>): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                newRelicApiMap.forEach {  (_, it) ->
                    val logString = gson.toJson(it)
                    val extraLog = JSONObject()
                    extraLog.put(JSON_LOG_KEY, logString)
                    ApmAgent.monitorEvent(SERVICE_NAME, extraLog, null, null)
                }
                true
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            false
        }
    }

    companion object {
        private const val SERVICE_NAME = "server_logger"
        private const val JSON_LOG_KEY = "json_log"
    }
}
