package com.tokopedia.logger.datasource.cloud

interface LoggerCloudNewRelicImpl {
    suspend fun sendToLogServer(message: List<String>) : Boolean
}