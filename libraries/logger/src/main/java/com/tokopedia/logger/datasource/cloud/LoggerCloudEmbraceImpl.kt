package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.EmbraceBody

interface LoggerCloudEmbraceImpl {
    suspend fun sendToLogServer(embraceBodyList: List<EmbraceBody>): Boolean
}