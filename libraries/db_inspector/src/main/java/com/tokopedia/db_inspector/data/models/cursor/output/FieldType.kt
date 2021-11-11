package com.tokopedia.db_inspector.data.models.cursor.output

internal enum class FieldType {
    NULL,
    INTEGER,
    FLOAT,
    STRING;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}
