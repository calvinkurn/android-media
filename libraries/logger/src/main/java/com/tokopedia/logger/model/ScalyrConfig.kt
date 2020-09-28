package com.tokopedia.logger.model

data class ScalyrConfig(
        val token: String = "",
        val session: String = "",
        val serverHost: String = "",
        val parser: String = "",
        val packageName: String = "",
        val debug: Boolean = false,
        val priority: Int = 0)