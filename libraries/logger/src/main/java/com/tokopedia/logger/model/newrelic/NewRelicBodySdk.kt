package com.tokopedia.logger.model.newrelic

data class NewRelicBodySdk(
    val eventType: String,
    val attributes: Map<String, Any>
): NewRelicBody {
    override val willUseSDK: Boolean = true
}
