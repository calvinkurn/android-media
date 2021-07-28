package com.tokopedia.logger.model

import com.tokopedia.logger.model.scalyr.ScalyrEvent

data class LoggerCloudModelWrapper(
        val scalyrMessageList: List<ScalyrEvent> = emptyList(),
        val newRelicMessageList: List<String> = emptyList(),
        val embraceMessageList: List<EmbraceBody> = emptyList()
)