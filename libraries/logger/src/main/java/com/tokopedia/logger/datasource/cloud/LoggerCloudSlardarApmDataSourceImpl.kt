package com.tokopedia.logger.datasource.cloud

import com.bytedance.apm.ApmAgent
import com.google.gson.Gson
import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoggerCloudSlardarApmDataSourceImpl: LoggerCloudSlardarApmDataSource {

    private val gson by lazy { Gson() }
    override suspend fun sendApmLogToServer(newRelicSdkList: List<NewRelicBodySdk>): Boolean {
        return withContext(Dispatchers.IO) {
            newRelicSdkList.forEach {
                val logString = gson.toJson(it)
                val category = JSONObject()
                category.put(JSON_LOG_KEY, logString)
                ApmAgent.monitorEvent(SERVICE_NAME, category, null, null)
            }
            true
        }
    }

    override suspend fun sendApmLogToServer(newRelicApiMap: Map<String, NewRelicBodyApi>): Boolean {
        return withContext(Dispatchers.IO) {
            newRelicApiMap.forEach {  (_, it) ->
                val logString = gson.toJson(it)
                val category = JSONObject()
                category.put(JSON_LOG_KEY, logString)
                ApmAgent.monitorEvent(SERVICE_NAME, category, null, null)
            }
            true
        }
    }

    companion object {
        private const val SERVICE_NAME = "server_logger"
        private const val JSON_LOG_KEY = "json_log"
    }
}
