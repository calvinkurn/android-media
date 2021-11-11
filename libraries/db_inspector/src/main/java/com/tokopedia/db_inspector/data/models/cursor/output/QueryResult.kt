package com.tokopedia.db_inspector.data.models.cursor.output

internal data class QueryResult(
    val rows: List<Row>,
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0
)
