package com.tokopedia.logger.model.scalyr

data class ScalyrConfig(
        val token: String = "",
        val session: String = "",
        val serverHost: String = "",
        val parser: String = "",
        val packageName: String = "",
        val installer: String = "",
        val debug: Boolean = false,
        val priority: Int = 0)