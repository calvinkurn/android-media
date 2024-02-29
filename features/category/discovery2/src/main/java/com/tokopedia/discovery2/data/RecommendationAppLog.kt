package com.tokopedia.discovery2.data

data class RecommendationAppLog(
    val logId: String,
    val requestId: String,
    val sessionId: String,
    val recParams: String,
    val pageName: String
)
