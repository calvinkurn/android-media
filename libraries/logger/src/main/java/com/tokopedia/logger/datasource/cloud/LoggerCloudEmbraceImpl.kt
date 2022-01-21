package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.embrace.EmbraceBody

interface LoggerCloudEmbraceImpl {
    suspend fun sendToLogServer(embraceBodyList: List<EmbraceBody>): Boolean
}