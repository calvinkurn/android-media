package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.model.EmbraceBody
import io.embrace.android.embracesdk.Embrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoggerCloudEmbraceDataSource : LoggerCloudEmbraceImpl {

    override suspend fun sendToLogServer(embraceBodyList: List<EmbraceBody>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                for (embrace in embraceBodyList) {
                    Embrace.getInstance().logInfo(embrace.tag, embrace.messageMap)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}