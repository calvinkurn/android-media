package com.tokopedia.logger.model.newrelic

data class NewRelicBody(val eventType: String, val attributes: Map<String, Any>)