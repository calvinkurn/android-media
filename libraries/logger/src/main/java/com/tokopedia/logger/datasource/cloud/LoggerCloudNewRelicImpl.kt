package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.newrelic.NewRelicConfig

interface LoggerCloudNewRelicImpl {
    suspend fun sendToLogServer(newRelicConfig: NewRelicConfig, message: List<String>) : Boolean
}