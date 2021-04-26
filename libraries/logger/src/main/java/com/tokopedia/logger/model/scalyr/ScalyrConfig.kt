package com.tokopedia.logger.model.scalyr

data class ScalyrConfig(
        val token: String = "",
        val session: String = "",
        val serverHost: String = "",
        val parser: String = "")