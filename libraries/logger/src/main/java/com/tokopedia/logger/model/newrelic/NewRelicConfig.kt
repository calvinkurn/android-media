package com.tokopedia.logger.model.newrelic

data class NewRelicConfig(
        val token: String = "",
        val packageName: String = "",
        val installer: String = "",
        val debug: Boolean = false,
        val priority: Int = 0)