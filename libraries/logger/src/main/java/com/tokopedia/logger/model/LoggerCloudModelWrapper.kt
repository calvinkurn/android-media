package com.tokopedia.logger.model

import com.tokopedia.logger.model.embrace.EmbraceBody
import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicBodySdk
import com.tokopedia.logger.model.scalyr.ScalyrEvent

data class LoggerCloudModelWrapper(
    val scalyrMessageList: List<ScalyrEvent> = emptyList(),
    val newRelicMessageSdkList: List<NewRelicBodySdk> = emptyList(),
    val newRelicMessageApiMap: Map<String, NewRelicBodyApi> = emptyMap(),
    val embraceMessageList: List<EmbraceBody> = emptyList()
)
