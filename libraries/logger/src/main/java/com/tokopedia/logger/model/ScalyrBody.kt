package com.tokopedia.logger.model

data class ScalyrBody(val token: String,
                      val session: String,
                      val sessionInfo: ScalyrSessionInfo,
                      val events: List<ScalyrEvent>)
data class ScalyrSessionInfo(val serverHost: String, val parser: String)
data class ScalyrEvent(val ts: Long, val attrs: ScalyrEventAttrs, val sev: Int = 3)
data class ScalyrEventAttrs(val message: String)