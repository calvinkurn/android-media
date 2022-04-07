package com.tokopedia.logger.model

import com.tokopedia.logger.model.embrace.EmbraceBody
import com.tokopedia.logger.model.newrelic.NewRelicBody
import com.tokopedia.logger.model.scalyr.ScalyrEvent

data class LoggerCloudModelWrapper(
        val scalyrMessageList: List<ScalyrEvent> = emptyList(),
        val newRelicMessageList: List<NewRelicBody> = emptyList(),
        val embraceMessageList: List<EmbraceBody> = emptyList()
)