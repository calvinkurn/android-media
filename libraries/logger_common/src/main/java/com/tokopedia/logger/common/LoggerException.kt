package com.tokopedia.logger.common

data class LoggerException(val priority: String, val tag: String, val dataException: Map<String, String>): Throwable()