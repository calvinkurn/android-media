package com.tokopedia.data_explorer.db_explorer.data.models.cursor.output

internal data class QueryResult(
    val rows: List<Row>,
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0
)

internal data class Row(
    val position: Int,
    val fields: List<Field>
)

internal data class Field(
    val type: FieldType,
    val text: String? = null,
)

internal enum class FieldType {
    NULL,
    INTEGER,
    FLOAT,
    STRING;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}


