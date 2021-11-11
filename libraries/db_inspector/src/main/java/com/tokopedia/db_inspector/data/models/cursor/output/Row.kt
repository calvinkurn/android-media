package com.tokopedia.db_inspector.data.models.cursor.output

internal data class Row(
    val position: Int,
    val fields: List<Field>
)
