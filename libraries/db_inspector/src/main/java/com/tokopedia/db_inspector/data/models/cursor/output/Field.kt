package com.tokopedia.db_inspector.data.models.cursor.output

internal data class Field(
    val type: FieldType,
    val text: String? = null,
   )
