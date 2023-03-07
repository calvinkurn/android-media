package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.newrelic.NewRelicBodySdk

interface LoggerCloudNewRelicSdkImpl {
    suspend fun sendToLogServer(newRelicBodyList: List<NewRelicBodySdk>) : Boolean
}
