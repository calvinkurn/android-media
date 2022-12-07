package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.newrelic.NewRelicConfig

interface LoggerCloudNewRelicApiImpl {
    suspend fun sendToLogServer(newRelicConfig: NewRelicConfig, message: List<String>): Boolean
}
