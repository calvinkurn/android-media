package com.tokopedia.logger.datasource.cloud

import com.bytedance.apm.ApmAgent
import com.google.gson.Gson
import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk
import org.json.JSONObject

class LoggerCloudSlardarApmDataSourceImpl: LoggerCloudSlardarApmDataSource {

    private val gson by lazy { Gson() }
    override suspend fun sendApmLogToServer(newRelicSdkList: List<NewRelicBodySdk>): Boolean {
        newRelicSdkList.forEach {
            val logString = gson.toJson(it)
            val category = JSONObject()
            category.put(JSON_LOG_KEY, logString)
            ApmAgent.monitorEvent(SERVICE_NAME, category, null, null)
        }
        return true
    }

    override suspend fun sendApmLogToServer(newRelicApiMap: Map<String, NewRelicBodyApi>): Boolean {
        newRelicApiMap.forEach {  (_, it) ->
            val logString = gson.toJson(it)
            val category = JSONObject()
            category.put(JSON_LOG_KEY, logString)
            ApmAgent.monitorEvent(SERVICE_NAME, category, null, null)
        }
        return true
    }

    companion object {
        private const val SERVICE_NAME = "server_logger"
        private const val JSON_LOG_KEY = "json_log"
    }
}
