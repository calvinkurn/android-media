package com.tokopedia.logger.model.newrelic

import androidx.annotation.Keep

@Keep
data class NewRelicBodyApi(
    val newRelicConfig: NewRelicConfig,
    val messageList: List<String>
)
