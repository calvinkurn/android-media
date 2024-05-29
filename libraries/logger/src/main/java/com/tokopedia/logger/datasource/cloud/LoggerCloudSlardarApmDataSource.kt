package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk

interface LoggerCloudSlardarApmDataSource {
    suspend fun sendApmLogToServer(newRelicSdkList: List<NewRelicBodySdk>): Boolean
    suspend fun sendApmLogToServer(newRelicApiMap: Map<String, NewRelicBodyApi>): Boolean
}
