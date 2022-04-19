package com.tokopedia.logger.datasource.cloud

import com.newrelic.agent.android.NewRelic
import com.tokopedia.logger.model.newrelic.NewRelicBody
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoggerCloudNewRelicDataSource : LoggerCloudNewRelicImpl {

    override suspend fun sendToLogServer(newRelicConfig: NewRelicConfig, newRelicBodyList: List<NewRelicBody>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                for (newRelic in newRelicBodyList) {
                    NewRelic.recordCustomEvent(newRelic.eventType, newRelic.attributes)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}