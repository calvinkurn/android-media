package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.logger.model.newrelic.NewRelicConfig

interface LoggerCloudNewRelicApiImpl {
    suspend fun sendToLogServer(gson: Gson, newRelicConfig: NewRelicConfig, message: List<String>): Boolean
}
