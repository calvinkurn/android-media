package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.pocnewrelic.datasource.NewRelicCloudDataSource
import javax.inject.Inject

class SendNewRelicDataUseCase @Inject constructor(
        private val newRelicCloudDataSource: NewRelicCloudDataSource
) {
    fun executeOnBackground(data: Map<String, Any>) {
        newRelicCloudDataSource.sendData(data)
    }
}