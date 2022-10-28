package com.tokopedia.data_explorer.db_explorer.data.sources.cursor

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.Field
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.FieldType
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.Row
import com.tokopedia.data_explorer.db_explorer.data.sources.cursor.shared.CursorSource

internal class PragmaSource : Sources.Pragma, CursorSource() {
    override suspend fun getUserVersion(query: Query): QueryResult {
        if (query.database?.isOpen == true) {
            runQuery(query)?.use { cursor ->
                val result = when (cursor.moveToFirst()) {
                    true -> cursor.getString(0)
                    false -> ""
                }
                return QueryResult(
                    rows = listOf(
                        Row(
                            position = 0,
                            fields = listOf(
                                Field(
                                    type = FieldType.STRING,
                                    text = result
                                )
                            )
                        )
                    )
                )
            } ?: throw IllegalStateException("Cannot obtain a raw query cursor.")
        } else throw IllegalStateException("Cannot perform a query using a closed database connection.")
    }

    override suspend fun getTableInfo(query: Query) : QueryResult {
        return collectRows(query)
    }

}