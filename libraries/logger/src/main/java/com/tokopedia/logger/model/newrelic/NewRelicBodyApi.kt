package com.tokopedia.logger.model.newrelic

data class NewRelicBodyApi(
    val newRelicConfig: NewRelicConfig,
    val messageList: List<String>,
): NewRelicBody {
    override val willUseSDK: Boolean = false
}
