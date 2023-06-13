package com.tokopedia.logger.model.newrelic

data class NewRelicBodyApi(
    val newRelicConfig: NewRelicConfig,
    val messageList: List<String>
)
