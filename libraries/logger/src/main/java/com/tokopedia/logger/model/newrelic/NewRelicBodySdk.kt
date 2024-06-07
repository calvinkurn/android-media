package com.tokopedia.logger.model.newrelic

import androidx.annotation.Keep

@Keep
data class NewRelicBodySdk(
    val eventType: String,
    val attributes: Map<String, Any>
)
