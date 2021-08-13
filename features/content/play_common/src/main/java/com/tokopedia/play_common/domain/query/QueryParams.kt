package com.tokopedia.play_common.domain.query


data class QueryParams(
        val query: String = "",
        val params: Map<String, Any> = emptyMap()
)