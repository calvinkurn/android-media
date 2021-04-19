package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.scalyr.ScalyrConfig
import com.tokopedia.logger.model.scalyr.ScalyrEvent

interface LoggerCloudDataSource {
    suspend fun sendLogToServer(config: ScalyrConfig, eventList: List<ScalyrEvent>): Boolean
}